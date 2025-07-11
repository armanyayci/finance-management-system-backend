package com.sau.swe.api;

import com.sau.swe.dto.GoalRequestDTO;
import com.sau.swe.entity.FinancialGoals;
import com.sau.swe.interfaces.UserService;
import com.sau.swe.utils.response.GenericResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> getUsers(@RequestParam(name = "id", required = false) Long id){
        if(id != null){
            return ResponseEntity.ok(userService.getUserById(id));
        }
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping("/add-image/{userId}")
    public ResponseEntity<GenericResponse<Void>> uploadProfileImage(
            @PathVariable("userId") Long userId,
            @RequestParam("file") MultipartFile file) {
        userService.addImage(userId, file);
        return ResponseEntity.ok(GenericResponse.success("user.profile.add.image.success"));
    }
    @PostMapping("/add-goal")
    public ResponseEntity<GenericResponse<Void>> addGoal(@RequestBody List<GoalRequestDTO> goals) {
        userService.addGoal(goals);
        return ResponseEntity.ok(GenericResponse.success("user.goal.added"));
    }
    @GetMapping("/get-goals/{userId}")
    public ResponseEntity<GenericResponse<List<FinancialGoals>>> getGoals(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(GenericResponse.success(userService.getFinancialGoals(userId)));
    }
    @DeleteMapping("/delete-goal/{goalId}")
    public ResponseEntity<GenericResponse<Void>> deleteGoal(@PathVariable("goalId") Long goalId) {
        userService.deleteGoal(goalId);
        return ResponseEntity.ok(GenericResponse.success("user.goal.deleted"));
    }
}
