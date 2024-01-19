package cj.software.genetics.schedule.server.api.controller;

import cj.software.genetics.schedule.server.entity.calculation.CalculatePostInput;
import cj.software.genetics.schedule.server.entity.calculation.CalculatePostOutput;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(
        path = "calculate",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE)
@Validated
@Api(
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE,
        value = "Taschenrechner",
        tags = {"mathematics"})
public class ArithmeticController {
    @ApiOperation(value = "calculate the sum of 2 numbers",
            response = CalculatePostOutput.class)
    @ApiResponse(
            code = 200,
            message = "successfully calculated",
            examples = @Example(@ExampleProperty(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    value = """
                            {
                                "result": 42
                            }
                            """))

    )
    @PostMapping(path = "sum", consumes = MediaType.APPLICATION_JSON_VALUE)
    @NotNull
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "calculatePostInput",
                    value = """
                            {
                                "number1": 42,
                                "number2": 5
                            }
                            """,
                    required = true,
                    dataTypeClass = CalculatePostInput.class,
                    paramType = "BODY",
                    examples = @Example({
                            @ExampleProperty(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    value = """
                                            {
                                                "number1": 42,
                                                "number2": 5
                                            }
                                            """)
                    })
            )
    })
    public CalculatePostOutput sum(
            @RequestBody
            @NotNull
            @Valid
            CalculatePostInput calculatePostInput) {
        int number1 = calculatePostInput.getNumber1();
        int number2 = calculatePostInput.getNumber2();
        int sum = number1 + number2;
        CalculatePostOutput result = new CalculatePostOutput(sum);
        return result;
    }
}
