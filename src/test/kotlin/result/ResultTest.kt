package result

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ResultTest {
    @Test
    fun `err() creates an Err(Unit)`() {
        assertThat(err()).isEqualTo(Err(Unit))
    }

    @Test
    fun `toOk() wraps the object in an Ok`() {
        assertThat("abc".toOk()).isEqualTo(Ok("abc"))
    }

    @Test
    fun `toErr() wraps the object in an Err`() {
        assertThat("abc".toErr()).isEqualTo(Err("abc"))
    }

    @Test
    fun `ok() creates an Ok(Unit)`() {
        assertThat(ok()).isEqualTo(Ok(Unit))
    }

    @Test
    fun `isOk returns true on Ok `() {
        // given
        val result: Result<Unit, String> = ok()

        // when
        val r = isOk(result)

        // then
        assertThat(r).isTrue
    }

    @Test
    fun `isOk returns false on Err `() {
        // given
        val result: Result<String, Unit> = err()

        // when
        val r = isOk(result)

        // then
        assertThat(r).isFalse
    }

    @Test
    fun `isErr returns true on Err `() {
        // given
        val result: Result<String, Unit> = err()

        // when
        val r = isErr(result)

        // then
        assertThat(r).isTrue
    }

    @Test
    fun `isErr returns false on Ok `() {
        // given
        val result: Result<Unit, String> = ok()

        // when
        val r = isErr(result)

        // then
        assertThat(r).isFalse
    }

    @Test
    fun `isOk being true smart casts result to Ok `() {
        // given
        val message = "hello world!"
        val result: Result<String, Unit> = Ok(message)

        // when & then
        if (isOk(result)) {
            assertThat(result.value).isEqualTo(message)
        }
    }

    @Test
    fun `isOk being false smart casts result to Err `() {
        // given
        val message = "hello world!"
        val result: Result<Unit, String> = Err(message)

        // when & then
        if (!isOk(result)) {
            assertThat(result.value).isEqualTo(message)
        }
    }

    @Test
    fun `isErr being true smart casts result to Err`() {
        // given
        val message = "hello world!"
        val result: Result<Unit, String> = Err(message)

        // when & then
        if (isErr(result)) {
            assertThat(result.value).isEqualTo(message)
        }
    }

    @Test
    fun `isErr being false smart casts result to Ok`() {
        // given
        val message = "hello world!"
        val result: Result<String, Unit> = Ok(message)

        // when & then
        if (!isErr(result)) {
            assertThat(result.value).isEqualTo(message)
        }
    }

    @Test
    fun `map maps an Ok result to a new value`() {
        val message1 = "hello world!"
        val message2 = "and goodbye world!"
        val nestedResult = Ok(value = message1) as Result<String, String>

        // when
        val r = nestedResult.map { it + message2 }

        // then
        assertThat(r.getOrThrow() == message1 + message2)
    }

    @Test
    fun `flatMap flattens nested Ok results`() {
        val message = "hello world!"
        val nestedResult = Ok(value = Ok(value = Ok(message)))

        // when
        val r = nestedResult.flatMap { it }.flatMap { it }

        // then
        assertThat(r.getOrThrow() == message)
    }

    @Test
    fun `mapErr maps an Err result to a new value`() {
        val message1 = "hello world!"
        val message2 = "and goodbye world!"
        val nestedResult = Err(value = message1) as Result<String, String>

        // when
        val r = nestedResult.mapErr { it + message2 }

        // then
        assertThat(r.getErrOrThrow() == message1 + message2)
    }

    @Test
    fun `flatMapErr flattens nested Err results`() {
        val message = "hello world!"
        val nestedResult = Err(value = Err(value = Err(message)))

        // when
        val r = nestedResult.flatMapErr { it }.flatMapErr { it }

        // then
        assertThat(r.getErrOrThrow() == message)
    }
}
