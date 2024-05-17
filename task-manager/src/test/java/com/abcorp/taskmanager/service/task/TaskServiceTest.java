package com.abcorp.taskmanager.service.task;

import com.abcorp.taskmanager.config.MailConfig;
import com.abcorp.taskmanager.exception.BadRequestException;
import com.abcorp.taskmanager.exception.NotFoundException;
import com.abcorp.taskmanager.model.entity.Task;
import com.abcorp.taskmanager.model.entity.User;
import com.abcorp.taskmanager.model.request.AddTaskDto;
import com.abcorp.taskmanager.model.response.TaskDto;
import com.abcorp.taskmanager.repository.TaskRepository;
import com.abcorp.taskmanager.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TaskServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    TaskRepository taskRepository;

    @MockBean
    private MailConfig mailConfig;

    @MockBean
    private JavaMailSender mailSender;

    @Test
    public void addTaskNullUserTest(){
        TaskService taskService = new TaskServiceImpl(taskRepository, userRepository);

        assertThrows(
                BadRequestException.class,
                () -> taskService.addTask(null,null)
        );
        verify(taskRepository, times(0)).save(any());
        verify(userRepository, times(0)).findById(any());
    }

    @Test
    public void addTaskUserNotFoundTest(){
        TaskService taskService = new TaskServiceImpl(taskRepository, userRepository);
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(
                NotFoundException.class,
                () -> taskService.addTask(UUID.randomUUID(),null)
        );
        verify(taskRepository, times(0)).save(any());
        verify(userRepository, times(1)).findById(any());
    }

    @Test
    public void addTaskSuccess(){
        TaskService taskService = new TaskServiceImpl(taskRepository, userRepository);
        when(userRepository.findById(any())).thenReturn(Optional.of(new User()));
        when(taskRepository.save(any())).thenReturn(new Task());
        taskService.addTask(UUID.randomUUID(),new AddTaskDto());
        verify(taskRepository, times(1)).save(any());
        verify(userRepository, times(1)).findById(any());
    }

    @Test
    public void getTaskByIdFail(){
        TaskService taskService = new TaskServiceImpl(taskRepository, userRepository);
        when(taskRepository.findByIdAndUserId(any(), any())).thenReturn(Optional.empty());
        assertThrows(
                NotFoundException.class,
                () -> taskService.getTaskById(UUID.randomUUID(), UUID.randomUUID())
        );
        verify(taskRepository, times(1)).findByIdAndUserId(any(), any());
    }

    @Test
    public void getTaskByIdSuccess(){
        TaskService taskService = new TaskServiceImpl(taskRepository, userRepository);
        when(taskRepository.findByIdAndUserId(any(), any())).thenReturn(Optional.of(new Task()));
        taskService.getTaskById(UUID.randomUUID(), UUID.randomUUID());
        verify(taskRepository, times(1)).findByIdAndUserId(any(), any());
    }

    @Test
    public void updateTaskFail(){
        TaskService taskService = new TaskServiceImpl(taskRepository, userRepository);
        when(taskRepository.findByIdAndUserId(any(), any())).thenReturn(Optional.empty());
        assertThrows(
                NotFoundException.class,
                () -> taskService.updateTask(UUID.randomUUID(), UUID.randomUUID(), new TaskDto())
        );

        verify(taskRepository, times(1)).findByIdAndUserId(any(), any());
        verify(taskRepository, times(0)).save(any());

    }

    @Test
    public void updateTaskSuccess(){
        TaskService taskService = new TaskServiceImpl(taskRepository, userRepository);
        when(taskRepository.findByIdAndUserId(any(), any())).thenReturn(Optional.of(new Task()));
        when(taskRepository.save(any())).thenReturn(new Task());
        taskService.updateTask(UUID.randomUUID(), UUID.randomUUID(), new TaskDto());
        verify(taskRepository, times(1)).findByIdAndUserId(any(), any());
        verify(taskRepository, times(1)).save(any());
    }

    @Test
    public void listTaskForUser(){
        TaskService taskService = new TaskServiceImpl(taskRepository, userRepository);
        when(taskRepository.findAllByUserId(any(), any())).thenReturn(Page.empty());
        taskService.listTasksForUser(UUID.randomUUID(), Pageable.ofSize(2));
        verify(taskRepository, times(1)).findAllByUserId(any(), any());
    }

    @Test
    public void deleteTaskFail(){
        TaskService taskService = new TaskServiceImpl(taskRepository, userRepository);
        when(taskRepository.findByIdAndUserId(any(), any())).thenReturn(Optional.empty());
        assertThrows(
                NotFoundException.class,
                () -> taskService.deleteTask(UUID.randomUUID(), UUID.randomUUID())
        );
        verify(taskRepository, times(1)).findByIdAndUserId(any(), any());
        verify(taskRepository, times(0)).delete(any());
    }

    @Test
    public void deleteTaskSuccess(){
        TaskService taskService = new TaskServiceImpl(taskRepository, userRepository);
        when(taskRepository.findByIdAndUserId(any(), any())).thenReturn(Optional.of(new Task()));
        taskService.deleteTask(UUID.randomUUID(), UUID.randomUUID());
        verify(taskRepository, times(1)).findByIdAndUserId(any(), any());
        verify(taskRepository, times(1)).delete(any());
    }
}
