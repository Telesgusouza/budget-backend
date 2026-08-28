package com.example.demo.recurringBill;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecurringBillService {

	@Autowired
	private RecurringBillRepository repo;

	public RecurringBill addNewRecurringBill(RecurringBillDTO data) {

		RecurringBill newRecurringBill = new RecurringBill(null, data.name(), data.value(),
				Instant.parse(data.dueDate()));
		RecurringBill save = this.repo.save(newRecurringBill);

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

		if (Instant.parse(data.dueDate()) != request.getDueDate()) {
			request.setDueDate(Instant.parse(data.dueDate()));

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

}
