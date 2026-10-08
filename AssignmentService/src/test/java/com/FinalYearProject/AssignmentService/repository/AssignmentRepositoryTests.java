package com.FinalYearProject.AssignmentService.repository;

import com.FinalYearProject.AssignmentService.model.Assignment;
import com.FinalYearProject.AssignmentService.model.AssignmentStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AssignmentRepositoryTests {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Test
    public void AssignmentRepository_findAll_returnFoundAgentAssigned() {

        //Arrange
        LocalDateTime localDateTime = LocalDateTime.of(2026, 9, 28, 14, 30, 49);
        LocalDateTime localDateTime2 = LocalDateTime.of(2026, 9, 28, 14, 30, 59);

        Assignment assignment = Assignment.builder()
                .orderId("1a")
                .assignedAt(localDateTime)
                .agentId("567jj")
                .updatedAt(localDateTime2)
                .failureReason("Broken")
                .build();

        Assignment savedAssignment = assignmentRepository.save(assignment);

        //Act
        Assignment findAgentId = assignmentRepository.findById(assignment.getId()).orElse(null);

        //Assert
        assertThat(findAgentId).isNotNull();
        assertThat(findAgentId.getAgentId()).isEqualTo(assignment.getAgentId());


    }

    @Test
    void findByStatus_returnsOnlyAssignmentsWithThatStatus() {

        //Arrange
        assignmentRepository.save(Assignment.builder()
                .orderId("1a")
                .agentId("agent-1")
                .status(AssignmentStatus.PENDING)
                .build());

        assignmentRepository.save(Assignment.builder()
                .orderId("2b")
                .agentId("agent-2")
                .status(AssignmentStatus.CANCELLED)
                .build());

        assignmentRepository.save(Assignment.builder()
                .orderId("3C")
                .agentId("agent-3")
                .status(AssignmentStatus.IN_TRANSIT)
                .build());

        assignmentRepository.save(Assignment.builder()
                .orderId("4d")
                .agentId("agent-4")
                .status(AssignmentStatus.ASSIGNED)
                .build());

        assignmentRepository.save(Assignment.builder()
                .orderId("5e")
                .agentId("agent-5")
                .status(AssignmentStatus.FAILED)
                .build());

        assignmentRepository.save(Assignment.builder()
                .orderId("6f")
                .agentId("agent-6")
                .status(AssignmentStatus.DELIVERED)
                .build());

        //Act
        List<Assignment> result = assignmentRepository.findByStatus(AssignmentStatus.PENDING);
        List<Assignment> result2 = assignmentRepository.findByStatus(AssignmentStatus.CANCELLED);
        List<Assignment> result3 = assignmentRepository.findByStatus(AssignmentStatus.IN_TRANSIT);
        List<Assignment> result4 = assignmentRepository.findByStatus(AssignmentStatus.ASSIGNED);
        List<Assignment> result5 = assignmentRepository.findByStatus(AssignmentStatus.FAILED);
        List<Assignment> result6 = assignmentRepository.findByStatus(AssignmentStatus.DELIVERED);


        //Assert
        assertThat(result).hasSize(1);
        assertThat(result).extracting(Assignment::getStatus).containsOnly(AssignmentStatus.PENDING);
        assertThat(result2).extracting(Assignment::getStatus).containsOnly(AssignmentStatus.CANCELLED);
        assertThat(result3).extracting(Assignment::getStatus).containsOnly(AssignmentStatus.IN_TRANSIT);
        assertThat(result4).extracting(Assignment::getStatus).containsOnly(AssignmentStatus.ASSIGNED);
        assertThat(result5).extracting(Assignment::getStatus).containsOnly(AssignmentStatus.FAILED);
        assertThat(result6).extracting(Assignment::getStatus).containsOnly(AssignmentStatus.DELIVERED);

        assertThat(result).extracting(Assignment::getOrderId).containsExactly("1a");

    }
}
