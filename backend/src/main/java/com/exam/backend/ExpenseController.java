package com.exam.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@CrossOrigin(origins = "*")
public class ExpenseController {

    @Autowired
    private ExpenseRepository repo;

    @GetMapping
    public List<Expense> getAll() {
        return repo.findAll();
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Expense expense) {
        if (expense.getTitle() == null || expense.getTitle().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Title cannot be empty.");
        }
        if (expense.getAmount() == null || expense.getAmount() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Amount must be greater than zero.");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(expense));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Expense details) {
        return repo.findById(id).map(expense -> {
            if (details.getTitle() == null || details.getTitle().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Title cannot be empty.");
            }
            if (details.getAmount() == null || details.getAmount() <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Amount must be greater than zero.");
            }
            expense.setTitle(details.getTitle());
            expense.setAmount(details.getAmount());
            expense.setCategory(details.getCategory());
            expense.setDate(details.getDate());
            return ResponseEntity.ok(repo.save(expense));
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found");
        }
        repo.deleteById(id);
        return ResponseEntity.ok("Deleted successfully");
    }
}