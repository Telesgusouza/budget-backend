package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import com.example.demo.budget.Budget;
import com.example.demo.budget.BudgetDTO;
import com.example.demo.budget.BudgetRepository;
import com.example.demo.budget.BudgetService;
import com.example.demo.budget.BudgetWithTransactionsDTO;
import com.example.demo.budget.CategoryEnum;
import com.example.demo.budget.ResponseHomeBudgetDTO;
import com.example.demo.exceptions.AuthenticationRequiredOrFailed;
import com.example.demo.exceptions.InvalidFieldException;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.transaction.TransactionRepository;
import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import com.example.demo.user.UserRole;

@ExtendWith(MockitoExtension.class)
public class BudgetServiceTest {

	@Mock
	private BudgetRepository budgetRepository;

	@Mock
	private TransactionRepository transactionRepository;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private BudgetService service;

	// getBudgets

	@Test
	public void retrieveListOfBudgets() {

		List<Budget> budgets = new ArrayList<>();

		User savedUser = new User(UUID.randomUUID(), "test@gmail.com", "senha-criptografada", UserRole.USER);

		when(budgetRepository.findByUserId(savedUser.getId())).thenReturn(budgets);

		List<BudgetWithTransactionsDTO> request = service.getBudgets(savedUser.getId());

		assertDoesNotThrow(() -> service.getBudgets(savedUser.getId()));
		assertNotNull(request);
	}

	@Test
	public void userNotFound() {

		UUID userId = UUID.randomUUID();

		when(budgetRepository.findByUserId(userId)).thenReturn(null);

		assertThrows(AuthenticationRequiredOrFailed.class, () -> service.getBudgets(userId));

	}

	// getHome
	@Test
	void shouldCalculateTotalsCorrectly() {

		Budget budget1 = new Budget();
		budget1.setBudget(1000f);
		budget1.setValueSpent(200f);
		budget1.setCategory(CategoryEnum.DINING_OUT);
		budget1.setColor("#FF0000");

		Budget budget2 = new Budget();
		budget2.setBudget(500f);
		budget2.setValueSpent(100f);
		budget2.setCategory(CategoryEnum.BILLS);
		budget2.setColor("#00FF00");

		User user = new User();
		user.setBudgets(List.of(budget1, budget2));

		ResponseHomeBudgetDTO response = service.getHome(user);

		assertEquals(300f, response.totalSpent());
		assertEquals(1500f, response.totalBudget());
		assertEquals(2, response.budgets().size());
	}

	@Test
	void shouldReturnEmptyValuesWhenUserHasNoBudgets() {

		User user = new User();
		user.setBudgets(Collections.emptyList());

		ResponseHomeBudgetDTO response = service.getHome(user);

		assertEquals(0f, response.totalSpent());
		assertEquals(0f, response.totalBudget());
		assertTrue(response.budgets().isEmpty());
	}

	// newbudget
	@Test
	void shouldCreateBudgetSuccessfully() {

		BudgetDTO dto = new BudgetDTO(CategoryEnum.DINING_OUT, 1000f, "#FFFFFF");

		User user = new User();
		user.setBudgets(new ArrayList<>());

		Budget savedBudget = new Budget(UUID.randomUUID(), 0.0f, 1000f, "#FFFFFF", CategoryEnum.DINING_OUT);

		when(budgetRepository.existsByUserAndCategory(user, CategoryEnum.DINING_OUT)).thenReturn(false);

		when(budgetRepository.save(any(Budget.class))).thenReturn(savedBudget);

		when(userRepository.save(user)).thenReturn(user);

		Budget result = service.newBudget(dto, user);

		assertNotNull(result);
		assertEquals(savedBudget, result);

		verify(budgetRepository).save(any(Budget.class));
		verify(userRepository).save(user);
	}

	@Test
	void shouldThrowExceptionWhenDataIsNull() {

		User user = new User();
		BudgetDTO data = null;

		InvalidFieldException exception = assertThrows(InvalidFieldException.class,
				() -> service.newBudget(data, user));

		assertEquals("Invalid field exception. Id Budget data cannot be null.", exception.getMessage());

	}

	@Test
	void shouldThrowExceptionWhenMaximumSpendIsNull() {

		BudgetDTO data = new BudgetDTO(CategoryEnum.DINING_OUT, null, "#FFF");

		User user = new User();

		InvalidFieldException exception = assertThrows(InvalidFieldException.class,
				() -> service.newBudget(data, user));

		assertEquals("Invalid field exception. Id The maximum value must be greater than zero.",
				exception.getMessage());
	}

	@Test
	void shouldThrowExceptionWhenMaximumSpendIsZero() {

		BudgetDTO data = new BudgetDTO(CategoryEnum.DINING_OUT, 0F, "#FFF");

		User user = new User();

		InvalidFieldException exception = assertThrows(InvalidFieldException.class,
				() -> service.newBudget(data, user));

		assertEquals("Invalid field exception. Id The maximum value must be greater than zero.",
				exception.getMessage());
	}

	@Test
	void shouldThrowExceptionWhenMaximumSpendIsNegative() {

		BudgetDTO data = new BudgetDTO(CategoryEnum.DINING_OUT, -100F, "#FFF");

		User user = new User();

		InvalidFieldException exception = assertThrows(InvalidFieldException.class,
				() -> service.newBudget(data, user));

		assertEquals("Invalid field exception. Id The maximum value must be greater than zero.",
				exception.getMessage());
	}

	@Test
	void shouldThrowExceptionWhenCategoryIsNull() {

		BudgetDTO data = new BudgetDTO(null, 100F, "#FFF");

		User user = new User();

		InvalidFieldException exception = assertThrows(InvalidFieldException.class,
				() -> service.newBudget(data, user));

		assertEquals("Invalid field exception. Id Mandatory category.", exception.getMessage());
	}

	@Test
	void shouldThrowExceptionWhenColorIsNull() {

		BudgetDTO data = new BudgetDTO(CategoryEnum.DINING_OUT, 100F, null);

		User user = new User();

		InvalidFieldException exception = assertThrows(InvalidFieldException.class,
				() -> service.newBudget(data, user));

		assertEquals("Invalid field exception. Id Mandatory color.", exception.getMessage());
	}

	@Test
	void shouldThrowExceptionWhenColorIsBlank() {

		BudgetDTO data = new BudgetDTO(CategoryEnum.DINING_OUT, 100F, "	");

		User user = new User();

		InvalidFieldException exception = assertThrows(InvalidFieldException.class,
				() -> service.newBudget(data, user));

		assertEquals("Invalid field exception. Id Mandatory color.", exception.getMessage());
	}

	@Test
	void shouldThrowExceptionWhenBudgetAlreadyExists() {

		BudgetDTO dto = new BudgetDTO(CategoryEnum.DINING_OUT, 1000f, "#FFFFFF");

		User user = new User();

		when(budgetRepository.existsByUserAndCategory(user, CategoryEnum.DINING_OUT)).thenReturn(true);

		InvalidFieldException exception = assertThrows(InvalidFieldException.class, () -> service.newBudget(dto, user));

		assertEquals("Invalid field exception. Id There is already a budget for this category.",
				exception.getMessage());

	}

	@Test
	void shouldCreateBudgetWhenUserBudgetListIsNull() {

		BudgetDTO dto = new BudgetDTO(CategoryEnum.DINING_OUT, 1000f, "#FFFFFF");

		User user = new User();
		user.setBudgets(null);

		Budget saved = new Budget();

		when(budgetRepository.existsByUserAndCategory(user, CategoryEnum.DINING_OUT)).thenReturn(false);

		when(budgetRepository.save(any())).thenReturn(saved);

		service.newBudget(dto, user);

		assertNotNull(user.getBudgets());
		assertEquals(1, user.getBudgets().size());
	}

	@Test
	void shouldThrowResourceNotFoundExceptionWhenSaveFails() {

		BudgetDTO dto = new BudgetDTO(CategoryEnum.DINING_OUT, 1000f, "#FFFFFF");

		User user = new User();
		user.setBudgets(new ArrayList<>());

		when(budgetRepository.existsByUserAndCategory(user, CategoryEnum.DINING_OUT)).thenReturn(false);

		when(budgetRepository.save(any())).thenThrow(new DataIntegrityViolationException("DB Error"));

		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
				() -> service.newBudget(dto, user));

		assertTrue(exception.getMessage().contains("Internal error while creating a quote."));
	}

	@Test
	void shouldThrowResourceNotFoundExceptionWhenUserSaveFails() {

		BudgetDTO dto = new BudgetDTO(CategoryEnum.DINING_OUT, 1000f, "#FFFFFF");

		User user = new User();
		user.setBudgets(new ArrayList<>());

		Budget budget = new Budget();

		when(budgetRepository.existsByUserAndCategory(user, CategoryEnum.DINING_OUT)).thenReturn(false);

		when(budgetRepository.save(any())).thenReturn(budget);

		when(userRepository.save(any())).thenThrow(new DataIntegrityViolationException("DB Error"));

		assertThrows(ResourceNotFoundException.class, () -> service.newBudget(dto, user));
	}

	// editBudget
	@Test
	void shouldUpdateCategory() {

		UUID id = UUID.randomUUID();

		Budget budget = new Budget();
		budget.setCategory(CategoryEnum.DINING_OUT);
		budget.setColor("#FFFFFF");
		budget.setValueSpent(1000f);

		BudgetDTO dto = new BudgetDTO(CategoryEnum.TRANSPORTATION, 1000f, "#FFFFFF");

		when(budgetRepository.findById(id)).thenReturn(Optional.of(budget));

		service.editBudget(dto, id);

		assertEquals(CategoryEnum.TRANSPORTATION, budget.getCategory());

		verify(budgetRepository).save(budget);
	}

	@Test
	void shouldUpdateMaximumSpend() {

		UUID id = UUID.randomUUID();

		Budget budget = new Budget();
		budget.setCategory(CategoryEnum.DINING_OUT);
		budget.setColor("#FFFFFF");
		budget.setValueSpent(1000f);

		BudgetDTO dto = new BudgetDTO(CategoryEnum.DINING_OUT, 2000f, "#FFFFFF");

		when(budgetRepository.findById(id)).thenReturn(Optional.of(budget));

		service.editBudget(dto, id);

		assertEquals(2000f, budget.getValueSpent());

		verify(budgetRepository).save(budget);
	}

	@Test
	void shouldUpdateColor() {

		UUID id = UUID.randomUUID();

		Budget budget = new Budget();
		budget.setCategory(CategoryEnum.DINING_OUT);
		budget.setColor("#FFFFFF");
		budget.setValueSpent(1000f);

		BudgetDTO dto = new BudgetDTO(CategoryEnum.DINING_OUT, 1000f, "#000000");

		when(budgetRepository.findById(id)).thenReturn(Optional.of(budget));

		service.editBudget(dto, id);

		assertEquals("#000000", budget.getColor());

		verify(budgetRepository).save(budget);
	}

	@Test
	void shouldUpdateAllFields() {

		UUID id = UUID.randomUUID();

		Budget budget = new Budget();
		budget.setCategory(CategoryEnum.DINING_OUT);
		budget.setColor("#FFFFFF");
		budget.setValueSpent(1000f);

		BudgetDTO dto = new BudgetDTO(CategoryEnum.TRANSPORTATION, 2500f, "#000000");

		when(budgetRepository.findById(id)).thenReturn(Optional.of(budget));

		service.editBudget(dto, id);

		assertEquals(CategoryEnum.TRANSPORTATION, budget.getCategory());

		assertEquals(2500f, budget.getValueSpent());

		assertEquals("#000000", budget.getColor());

		verify(budgetRepository).save(budget);
	}

	@Test
	void shouldThrowExceptionWhenBudgetDoesNotExist() {

		UUID id = UUID.randomUUID();

		BudgetDTO dto = new BudgetDTO(CategoryEnum.DINING_OUT, 1000f, "#FFFFFF");

		when(budgetRepository.findById(id)).thenReturn(Optional.empty());

		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
				() -> service.editBudget(dto, id));

		assertEquals("Resource not found. Id Budget not found", exception.getMessage());

	}

	@Test
	void shouldThrowExceptionWhenNothingChanges() {

		UUID id = UUID.randomUUID();

		Budget budget = new Budget();
		budget.setCategory(CategoryEnum.DINING_OUT);
		budget.setColor("#FFFFFF");
		budget.setValueSpent(1000f);

		BudgetDTO dto = new BudgetDTO(CategoryEnum.DINING_OUT, 1000f, "#FFFFFF");

		when(budgetRepository.findById(id)).thenReturn(Optional.of(budget));

		InvalidFieldException exception = assertThrows(InvalidFieldException.class, () -> service.editBudget(dto, id));

		assertEquals("Invalid field exception. Id There are no changes to be made.", exception.getMessage());
	}

	@Test
	void shouldSaveOnlyOnce() {

		UUID id = UUID.randomUUID();

		Budget budget = new Budget();
		budget.setCategory(CategoryEnum.DINING_OUT);
		budget.setColor("#FFFFFF");
		budget.setValueSpent(1000f);

		BudgetDTO dto = new BudgetDTO(CategoryEnum.TRANSPORTATION, 1000f, "#FFFFFF");

		when(budgetRepository.findById(id)).thenReturn(Optional.of(budget));

		service.editBudget(dto, id);

		verify(budgetRepository, times(1)).save(budget);
	}

	// deleteBudget
	@Test
	public void budgetDeletedSuccessfully() {
		UUID id = UUID.randomUUID();

		Budget budget = new Budget();
		budget.setCategory(CategoryEnum.DINING_OUT);
		budget.setColor("#FFFFFF");
		budget.setValueSpent(1000f);

		when(budgetRepository.findById(id)).thenReturn(Optional.of(budget));

		doNothing().when(budgetRepository).delete(any(Budget.class));

		service.deleteBudget(id);
		verify(budgetRepository).delete(budget);

	}

	@Test
	public void deleteDidNotFindTheBudgetByID() {
		UUID id = UUID.randomUUID();

		when(this.budgetRepository.findById(id)).thenReturn(Optional.empty());

		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
				() -> service.deleteBudget(id));

		assertEquals(exception.getMessage(), "Resource not found. Id Budget not found");

	}

}
