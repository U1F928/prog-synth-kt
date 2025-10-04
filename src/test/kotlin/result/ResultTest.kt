package result

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class ResultTest {
    @ParameterizedTest
    @CsvSource("true", "false")
    fun `test Result handling`(value: Boolean) {
        val x =
            if (value == true) {
                Ok("yay")
            } else {
                Err("nope")
            }

        x
            .map { it + "a" }
            .map { if (it == "yay a") it + "c" else Err("nope") }
    }

    @Test
    fun `flatMap nested Ok result`() {
        val nestedResult: Result<Result<String, String>, String> = Ok(value = Ok(value = "hello"))

        // when
        val r =
            nestedResult
                .flatMap { it }
                .map { it }

        // then
        check(r is Ok)
        assertThat(r.value == "hello world!")
    }
}
