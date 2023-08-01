package com.attraya.springboot.controller;

import com.attraya.springboot.dto.EmployeeDto;
import com.attraya.springboot.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.mockito.BDDMockito;

import java.util.Collections;
import java.util.List;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = EmployeeController.class)
public class EmployeeControllerTests {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private EmployeeService employeeService;

    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnSavedEmployee(){
        // given - pre-condition or setup
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName("Ramesh");
        employeeDto.setLastName("Fadatare");
        employeeDto.setEmail("ramesh@gmail.com");

        BDDMockito.given(employeeService.saveEmployee(ArgumentMatchers.any(EmployeeDto.class)))
                .willReturn(Mono.just(employeeDto));

        // when - action or behavior
        WebTestClient.ResponseSpec response = webTestClient.post().uri("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(employeeDto), EmployeeDto.class)
                .exchange();

        // then - verify the result or output
        response.expectStatus().isCreated()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.firstName").isEqualTo(employeeDto.getFirstName())
                .jsonPath("$.lastName").isEqualTo(employeeDto.getLastName())
                .jsonPath("$.email").isEqualTo(employeeDto.getEmail());
    }

    @Test
    public void givenEmployeeId_whenGetEmployee_thenReturnEmployeeObject() {
        // given - pre-condition or setup
        String employeeId = "123";

        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName("Ramesh");
        employeeDto.setLastName("Fadatare");
        employeeDto.setEmail("ramesh@gmail.com");

        BDDMockito.given(employeeService.getEmployee(employeeId))
                .willReturn(Mono.just(employeeDto));

        // when - action or behavior
        WebTestClient.ResponseSpec response = webTestClient.get()
                .uri("/api/employees/{id}", Collections.singletonMap("id", employeeId))
                .exchange();

        // then - verify the result or output
        response.expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.firstName").isEqualTo(employeeDto.getFirstName())
                .jsonPath("$.lastName").isEqualTo(employeeDto.getLastName())
                .jsonPath("$.email").isEqualTo(employeeDto.getEmail());
    }

    @Test
    public void givenListOfEmployees_whenGetAllEmployees_thenReturnListOfEmployees(){
        // given - pre-condition or setup
        EmployeeDto employeeDto1 = new EmployeeDto();
        employeeDto1.setFirstName("Ramesh");
        employeeDto1.setLastName("Fadatare");
        employeeDto1.setEmail("ramesh@gmail.com");

        EmployeeDto employeeDto2 = new EmployeeDto();
        employeeDto2.setFirstName("Tony");
        employeeDto2.setLastName("Stark");
        employeeDto2.setEmail("tony.stark@gmail.com");

        List<EmployeeDto> list = List.of(employeeDto1, employeeDto2);

        Flux<EmployeeDto> employeeFlux = Flux.fromIterable(list);

        BDDMockito.given(employeeService.getAllEmployees())
                .willReturn(employeeFlux);

        // when - action or behavior
        WebTestClient.ResponseSpec response = webTestClient.get().uri("/api/employees")
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        // then - verify the result or output
        response.expectStatus().isOk()
                .expectBodyList(EmployeeDto.class)
                .consumeWith(System.out::println);
    }

    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdatedEmployeeObject(){
        // given - pre-condition or setup
        String employeeId = "123";

        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName("Ramesh");
        employeeDto.setLastName("Fadatare");
        employeeDto.setEmail("ramesh@gmail.com");

        BDDMockito.given(employeeService.updateEmployee(ArgumentMatchers.any(EmployeeDto.class),
                ArgumentMatchers.any(String.class)))
                .willReturn(Mono.just(employeeDto));

        // when - action or behavior
        WebTestClient.ResponseSpec response = webTestClient.put().uri("/api/employees/{id}", Collections.singletonMap("id", employeeId))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(employeeDto), EmployeeDto.class)
                .exchange();

        // then - verify the result or output
        response.expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.firstName").isEqualTo(employeeDto.getFirstName())
                .jsonPath("$.lastName").isEqualTo(employeeDto.getLastName())
                .jsonPath("$.email").isEqualTo(employeeDto.getEmail());
    }

    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturnNothing(){
        // given - pre-condition or setup
        String employeeId = "123";
        Mono<Void> voidMono = Mono.empty();
        BDDMockito.given(employeeService.deleteEmployee(employeeId))
                .willReturn(voidMono);

        // when - action or behavior
        WebTestClient.ResponseSpec response = webTestClient
                .delete()
                .uri("/api/employees/{id}", Collections.singletonMap("id", employeeId))
                .exchange();

        // then - verify the result or output
        response.expectStatus().isNoContent()
                .expectBody()
                .consumeWith(System.out::println);
    }
}