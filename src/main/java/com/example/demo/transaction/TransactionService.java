package com.example.demo.transaction;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.user.User;
import com.example.demo.user.UserRepository;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

@Service
public class TransactionService {

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private UserRepository userRepository;

	// ATENÇÃO ler arquivo

	public Transaction extract(MultipartFile file, otherPaymentReceiptDetailsDTO data, User user) throws Exception {

		String filename = file.getOriginalFilename();

		if (filename.toLowerCase().endsWith(".pdf")) {
			
			KeyDetailsOfThePaymentReceiptDTO extractionOfProof = extractPDF(file, data, user);
			
			Transaction transaction = new Transaction(
					null, 
					extractionOfProof.name(), 
					extractionOfProof.value(),
					data.statusTransaction(), 
					extractionOfProof.date(), 
					data.category());
			
			Transaction response = saveTransaction(transaction, user);
			
			return response;
		} else {

			KeyDetailsOfThePaymentReceiptDTO extractionOfProof =  extractImage(file);

//			Transaction transaction = new Transaction(
//					null, 
//					extractionOfProof.name(), 
//					extractionOfProof.value(),
//					data.statusTransaction(), 
//					extractionOfProof.date(), 
//					data.category());
//			
//			Transaction response = saveTransaction(transaction, user);
//			
//			return response;
			
			return null;
			
		}
	}

	public KeyDetailsOfThePaymentReceiptDTO extractPDF(MultipartFile file, otherPaymentReceiptDetailsDTO data, User user)
			throws IOException {

		PDDocument document = Loader.loadPDF(new RandomAccessReadBuffer(file.getInputStream()));
		PDFTextStripper stripper = new PDFTextStripper();
		String textExtracted = stripper.getText(document);

		textExtracted = textExtracted.replaceAll("\\s+", " ").trim();

		String isPicpay = extractWithRegex(textExtracted, "PICPAY ID da transação", 0);
		if (isPicpay != "")
			return extractPicpay(textExtracted);

		String isBradesco = extractWithRegex(textExtracted, "Transação concluída pelo BRADESCO", 0);
		if (isBradesco != "")
			return extractBradesco(textExtracted);

		return null;
	}

	public KeyDetailsOfThePaymentReceiptDTO extractImage(MultipartFile file) {

		try {

			Tesseract tesseract = new Tesseract();

			tesseract.setDatapath("/usr/share/tesseract-ocr/4.00/tessdata");
			tesseract.setLanguage("por");

			BufferedImage image = ImageIO.read(file.getInputStream());
			String text = tesseract.doOCR(image);
			
			text = text.replaceAll("\\s+", " ").trim();
//			
//			System.out.println();
//			System.out.println();
//			
//			System.out.println("========================");
//			System.out.println(text);
//			
//			System.out.println();
//			System.out.println();
			
			String isNubank = extractWithRegex(text, "nubank.com.br/contatostouvidoria", 0);
			String is99Pay = extractWithRegex(text, "Detalhes do remetente Instituição 99Pay", 0);
			String isC6Bank = extractWithRegex(text, "C6bsN« Pix em Pix andamento realizado", 0);
			String isSantander = extractWithRegex(text, "Santander Comprovante do pagamento ", 0);
			String isUber = extractWithRegex(text, "Uber Conta | »digio ", 0);
			
			if (isNubank != "") {
				return extractNubank(text);
			} else if (is99Pay != "") {
				return extract99Pay(text);
			} else if (isC6Bank != "") {
				return extractC6Bank(text);
			} else if (isSantander != "") {
				return extractSantander(text);
			} else if (isUber != "") {
				return extractUber(text);
			}
			


			return null;
		} catch (Exception e) {
			// TODO: handle exception
		}

		return null;

	}
	
	private KeyDetailsOfThePaymentReceiptDTO extractNubank(String text) {
		
		String destinationName = extractWithRegex(text, "Destino\\s+Nome\\s+([A-Za-zÀ-ÿ\\s]+?)\\s+CPF", 1);

		String extractValue = extractWithRegex(text, "Valor\\s+(R\\$\\s?\\d{1,3}(?:\\.\\d{3})*,\\d{2})", 1)
				.replace("R$", "")
				.replace(",", ".");
		String extractDate = extractWithRegex(text, "(\\d{2}\\s+[A-Z]{3}\\s+\\d{4})", 1)
				.replace(" ", "/");
		Instant date = formatDate(extractDate);
		Float value = Float.parseFloat(extractValue);
		
		KeyDetailsOfThePaymentReceiptDTO response = new KeyDetailsOfThePaymentReceiptDTO(destinationName, value, date);
		
		return response;
	}
	
	private KeyDetailsOfThePaymentReceiptDTO extract99Pay(String text){
		
		String name = extractWithRegex(text, "Transferir\\s+para\\s+([A-Za-zÀ-ÿ\\s]+?)\\s+Data", 1);
		
		String extractValue = extractWithRegex(text, "concluída\\s+LR\\s+(R\\$\\s?\\d{1,3}(?:\\.\\d{3})*,\\d{2})", 1)
				.replace("R$", "")
				.replace(",", ".");
		String extractDate = extractWithRegex(text, "Data\\s+de\\s+pagamento\\s+(\\d{2}/\\d{2}/\\d{2})", 1);
		extractDate = extractDate.replaceAll("(\\d{2}/\\d{2}/)(\\d{2})", "$120$2");
		
		Float value = Float.parseFloat(extractValue);
		Instant date = this.formatDate(extractDate);
		
		KeyDetailsOfThePaymentReceiptDTO response = new KeyDetailsOfThePaymentReceiptDTO(name, value, date);
		
		return response;
	}
	
	private KeyDetailsOfThePaymentReceiptDTO extractC6Bank(String text){
		String name = extractWithRegex(text, "es\\s+([A-Za-zÀ-ÿ\\s]+?)\\s+Banco", 1);

		String extractValue = extractWithRegex(text, "Valor\\s+(R\\$\\s?\\d{1,3}(?:\\.\\d{3})*,\\d{2})", 1)
				.replace("R$", "")
				.replace(",", ".");
		String extractDate = extractWithRegex(text, "andamento\\s+realizado!\\s+(\\d{2}/\\d{2}/\\d{4})", 1);
		
		Float value = Float.parseFloat(extractValue);
		Instant date = formatDate(extractDate);
		
		KeyDetailsOfThePaymentReceiptDTO response = new KeyDetailsOfThePaymentReceiptDTO(name, value, date);

		return response;
	}
	
	private KeyDetailsOfThePaymentReceiptDTO extractSantander(String text){
		
		String name = extractWithRegex(text, " Dados\\s+do\\s+recebedor\\s+Para\\s+([A-Za-zÀ-ÿ\\s]+?)\\s+CPF", 1);
		String extractValue = extractWithRegex(text, "Valor\\s+pago\\s+(R\\$\\s?\\d{1,3}(?:\\.\\d{3})*,\\d{2})", 1)
				.replace("R$", "")
				.replace(",", ".");
		String extractDate = extractWithRegex(text, "Comprovante\\s+do\\s+pagamento\\s+(\\d{2}/\\d{2}/\\d{4})", 1);
		
		Float value = Float.parseFloat(extractValue);
		Instant date = formatDate(extractDate);
		
		KeyDetailsOfThePaymentReceiptDTO response = new KeyDetailsOfThePaymentReceiptDTO(name, value, date);
		
		return response;
	}
	
	

	
	
	private KeyDetailsOfThePaymentReceiptDTO extractUber(String text){
	
		String name = extractWithRegex(text, "QUEM\\s+RECEBEU\\s+Nome\\s+([A-Za-zÀ-ÿ\\s]+?)\\s+CPF", 1);
		
		if (name == "") {
			// CNPJ
			name = extractWithRegex(text, "QUEM\\s+RECEBEU\\s+Nome\\s+([A-Za-zÀ-ÿ\\s]+?)\\s+CNPJ", 1);
		}
		
		String extractValue = extractWithRegex(text, "Valor\\s+(R\\$\\s?\\d{1,3}(?:\\.\\d{3})*,\\d{2})", 1)
				.replace("R$", "")
				.replace(",", ".");
		
		String extractDate = extractWithRegex(text, "Transferido\\s+em\\s+(\\d{2}/\\d{2}/\\d{4})", 1);
		
		// date
		
		
		Float value = Float.parseFloat(extractValue);
		
		System.out.println();
		System.out.println();
		
		System.out.println("===================");
		System.out.println(extractDate);
		
		System.out.println();
		System.out.println();
		
		return null;
	}
	
	/*
		 
	 X Q Uber Conta | »digio Comprovante de transferência SOBRE A TRANSAÇÃO Transferido em 16/09/2026 às 11:34 
	 Valor R$ 4,00 Chave Pix Chave Pix não utilizada Código da transação a E27098060202609161434wfnBTC6mgGKkK 
	 QUEM PAGOU Nome GUSTAVO TELES DE SOUZA CPF ++ 0953.828-*+ Instituição Banco Digio 
	 QUEM RECEBEU Nome ACOUGUE JANGADEIRO LTDA CNPJ 62.000.666/0001-05 Instituição Itaú Unibanco S.A. 
	 Banco Digio S.A. 27098.060/0001-45 www.digio.com.br

	 
	 
	*/
	
	
	
	
	private KeyDetailsOfThePaymentReceiptDTO extractPicpay(
			String text 
			) {

		text = text.replaceAll("\\s+", " ").trim();

		String destinationName = extractWithRegex(text, "Para\\s+([A-Za-zÀ-ÖØ-öø-ÿ\\s]+)\\s+\\*+[^\\s]*", 1);

		if (destinationName == "") {
			destinationName = extractWithRegex(text,
					"Para\\s+([A-ZÀ-Ú0-9\\s\\.]+?)\\s*\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}[\\-–—]\\d{2}", 1);
		}

		String dateTime = extractWithRegex(text, "Pix\\s+(\\d{2}/[A-Za-z]{3}/\\d{4})\\s+-", 1).trim();

		Instant dateFormat = formatDate(dateTime);

		String value = extractWithRegex(text, "Valor (R\\$\\s*\\d{1,3}(\\.\\d{3})*,\\d{2})", 1);
		String valueFormat = value.replace("R$ ", "").replace(",", ".");
		
		KeyDetailsOfThePaymentReceiptDTO response = new KeyDetailsOfThePaymentReceiptDTO(destinationName, Float.parseFloat(valueFormat), dateFormat);
		
		return response;
	}

	private KeyDetailsOfThePaymentReceiptDTO extractBradesco(String text) {

		text = text.replaceAll("\\s+", " ").trim();

		String destinationName = extractWithRegex(text, "DADOS DE QUEM VAI RECEBER\\s+Nome:\\s*([^\\n]+?)\\s+CPF", 1);

		String extractValue = extractWithRegex(text, "DADOS DO PAGAMENTO\\s+Valor:\\s*([^\\n]+?)\\s+Data", 1);
		Float value = Float.parseFloat(extractValue.replace("R$ ", " ").replace(".", "").replace(",", "."));

		String extractDate = extractWithRegex(text, "Data e Hora:\\s+(\\d{2}/\\d{2}/\\d{4})\\s+-", 1);
		Instant date = formatDate(extractDate);
		
		KeyDetailsOfThePaymentReceiptDTO response = new KeyDetailsOfThePaymentReceiptDTO(destinationName, value, date);

		return response;
	}

	private String extractWithRegex(String text, String regex, Integer group) {

		Pattern valuePattern = Pattern.compile(regex);
		Matcher valueMatcher = valuePattern.matcher(text);

		if (valueMatcher.find()) {
			if (group == 1) {
				return valueMatcher.group(1).trim();
			}

			return valueMatcher.group();
		} else {
			return "";
		}

	}

	private Instant formatDate(String dateTime) {
		String date = dateTime.replace("/jan/", "/01/").replace("/fev/", "/02/").replace("/mar/", "/03/")
				.replace("/abr/", "/04/").replace("/mai/", "/05/").replace("/jun/", "/06/").replace("/jul/", "/07/")
				.replace("/ago/", "/08/").replace("/set/", "/09/").replace("/out/", "/10/").replace("/nov/", "/11/")
				.replace("/dez/", "/12/");

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.forLanguageTag("pt-BR"));
		LocalDate localDate = LocalDate.parse(date, formatter);
		return localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
	}
		
	private Transaction saveTransaction(Transaction transaction, User user) {
		transaction.setUser(user);
		
		Transaction response = this.transactionRepository.save(transaction);
		user.getTransactions().add(response);
		this.userRepository.save(user);
		
		return response;
	}

	//

	public Page<Transaction> getTransactionsPageable(Pageable pageable) {

		return this.transactionRepository.findAll(pageable);
	}

	public List<TransactionsResponseHomeDTO> getForHomeTransactions(User user) {

		List<TransactionsResponseHomeDTO> list = new ArrayList<>();

		int max = Math.min(user.getTransactions().size(), 6);

		for (int i = 0; i < max; i++) {
			if (user.getTransactions().get(i) != null) {
				Transaction currentTransaction = user.getTransactions().get(i);

				list.add(new TransactionsResponseHomeDTO(currentTransaction.getId(), currentTransaction.getName(),
						currentTransaction.getDate(), currentTransaction.getValue(),
						currentTransaction.getTransactionStatus()));
			}
		}

		return list;
	}

	public Transaction addNewTransaction(TransactionDTO data, User user) {

		Instant currentTime = Instant.now();

		Transaction newTransaction = new Transaction(null, data.name(), data.value(), data.statusValue(), currentTime,
				data.category());
		newTransaction.setUser(user);

		Transaction save = this.transactionRepository.save(newTransaction);

		user.getTransactions().add(save);

		this.userRepository.save(user);

		return save;
	}

	public void editTransaction(TransactionDTO data, UUID id) {

		Transaction requestTransaction = this.transactionRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Transaction not found"));

		boolean changed = false;

		if (!Objects.equals(data.name(), requestTransaction.getName())) {
			requestTransaction.setName(data.name());
			changed = true;
		}

		if (!Objects.equals(data.category(), requestTransaction.getCategory())) {
			requestTransaction.setCategory(data.category());
			changed = true;
		}

		if (!Objects.equals(data.value(), requestTransaction.getValue())) {
			requestTransaction.setValue(data.value());
			changed = true;
		}

		if (!Objects.equals(data.statusValue(), requestTransaction.getTransactionStatus())) {
			requestTransaction.setTransactionStatus(data.statusValue());
			changed = true;
		}

		if (!changed) {
			throw new RuntimeException("There are no changes to be made.");
		}

		this.transactionRepository.save(requestTransaction);

	}

	public void deleteTransaction(UUID id) {

		Transaction request = this.transactionRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Transaction not found"));
		this.transactionRepository.delete(request);

	}

}
