package com.example.demo.transaction;

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
			return extractPDF(file, data, user);
		} else {

//			String fullText = callOcrSpace(file);
//			return parseText(fullText);

//			return "daqui a pouco leremos image";
			return null;
		}
	}

	public Transaction extractPDF(MultipartFile file, otherPaymentReceiptDetailsDTO data, User user)
			throws IOException {

		PDDocument document = Loader.loadPDF(new RandomAccessReadBuffer(file.getInputStream()));
		PDFTextStripper stripper = new PDFTextStripper();
		String textExtracted = stripper.getText(document);

		textExtracted = textExtracted.replaceAll("\\s+", " ").trim();
		String isPicpay = extractWithRegex(textExtracted, "PICPAY ID da transação", 0);

		if (isPicpay != "") {
			return extractPicpay(textExtracted, data, user);
		}

		return null;
	}

	private Transaction extractPicpay(String text, otherPaymentReceiptDetailsDTO data, User user) {

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

		Transaction transaction = new Transaction(null, destinationName, Float.parseFloat(valueFormat),
				data.statusTransaction(), dateFormat, data.category());
		transaction.setUser(user);

		Transaction save = this.transactionRepository.save(transaction);
		user.getTransactions().add(save);
		this.userRepository.save(user);

		return save;
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
