package com.viewpoint.dangder.usecase

import com.google.common.truth.Truth.assertThat
import com.viewpoint.dangder.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.given

@DisplayName("EmailVerifyUseCase (이메일 검증 유스케이스)는")
internal class CreateEmailTokenUseCaseTest {
    @Mock
    private val mockAuthRepository = Mockito.mock(AuthRepository::class.java)
    private val createEmailTokenUseCase = CreateEmailTokenUseCase(mockAuthRepository)
    private val mainThread = newSingleThreadContext(CreateEmailTokenUseCase::class.java.simpleName)

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThread)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Nested
    @DisplayName("올바른 이메일을 입력하면")
    inner class ContextWithCollectEmail() {

        val email = "tt@tt.com"
        @BeforeEach
        fun setUp() = runTest {
            given(mockAuthRepository.createEmailTokenForSignUp(eq(email))).willReturn(true)
        }

        @Test
        @DisplayName("true를 리턴한다.")
        fun `it return true`() = runTest {

            val result = createEmailTokenUseCase(email, "signUp")
            assertThat(result).isTrue()
        }
    }

    @Nested
    @DisplayName("올바르지 않은 이메일을 입력하면")
    inner class ContextWithUnCollectEmail() {
        val email = "uncollect@tt.com"
        @BeforeEach
        fun setUp() = runTest {
            given(mockAuthRepository.createEmailTokenForSignUp(eq(email))).willReturn(false)
        }

        @Test
        @DisplayName("false를 리턴한다.")
        fun `it return false`() = runTest {
            val result = createEmailTokenUseCase(email, "signUp")
            assertThat(result).isFalse()
        }
    }


}