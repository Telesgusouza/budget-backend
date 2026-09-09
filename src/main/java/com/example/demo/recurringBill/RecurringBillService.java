package com.example.demo.recurringBill;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.user.User;
import com.example.demo.user.UserRepository;

@Service
public class RecurringBillService {

	@Autowired
	private RecurringBillRepository repo;

	@Autowired
	private UserRepository userRepository;

	public List<RecurringBill> searchRecurringBills(UUID userId, String name) {
		return repo.findByUserIdAndNameContainingIgnoreCase(userId, name);
	}

	public RecurringbillHomeDTO calcularTotais(User user) {
		List<RecurringBill> bills = user.getRecurringBills();

		float total = 0f;
		float overdue = 0f;
		float notYetDue = 0f;

		LocalDate today = LocalDate.now();

		for (RecurringBill bill : bills) {
			total += bill.getValue();

			// Verifica se a fatura já venceu
			LocalDate invoiceDueDate = LocalDate.of(today.getYear(), today.getMonth(), bill.getDueDate());

			// Se o dia de vencimento já passou neste mês
			if (invoiceDueDate.isBefore(today)) {
				// Se não foi paga ainda
				if (bill.getLastBillPaid() == null
						|| bill.getLastBillPaid().isBefore(invoiceDueDate.atStartOfDay().toInstant(ZoneOffset.UTC))) {
					overdue += bill.getValue();
				}
			} else {
				notYetDue += bill.getValue();
			}
		}

		RecurringbillHomeDTO response = new RecurringbillHomeDTO(total, notYetDue, overdue);

		return response;
	}

	public RecurringBill addNewRecurringBill(RecurringBillDTO data, User user) {

		RecurringBill newRecurringBill = new RecurringBill(null, data.name(), data.value(), data.dueDate(), null);
		newRecurringBill.setUser(user);

		RecurringBill save = this.repo.save(newRecurringBill);

		user.getRecurringBills().add(save);

		this.userRepository.save(user);

		return save;
	}

	public void editRecurringBill(RecurringBillDTO data, UUID id) {

		RecurringBill request = this.repo.findById(id)
				.orElseThrow(() -> new RuntimeException("Recurring Bill not found"));

		boolean changed = false;

		if (data.name() != request.getName()) {
			request.setName(data.name());

			changed = true;
		}

		if (data.dueDate() != request.getDueDate()) {
			request.setDueDate(data.dueDate());

			changed = true;
		}

		if (data.value() != request.getValue()) {
			request.setValue(data.value());

			changed = true;
		}

		if (!changed) {
			throw new RuntimeException("There are no changes to be made.");
		}

		this.repo.save(request);
	}

	public void deleteRecurringBills(UUID id) {

		RecurringBill request = this.repo.findById(id)
				.orElseThrow(() -> new RuntimeException("Recurring Bills not found"));
		this.repo.delete(request);

	}

	public Page<RecurringBill> getRecurringBillsPageable(Pageable pageable) {

		return this.repo.findAll(pageable);
	}

}
