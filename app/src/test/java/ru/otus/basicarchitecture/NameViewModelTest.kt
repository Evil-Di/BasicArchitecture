package ru.otus.basicarchitecture

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import ru.otus.basicarchitecture.data.PersonName
import ru.otus.basicarchitecture.data.WizardCache
import ru.otus.basicarchitecture.ui.name.NameViewModel
import java.time.LocalDate

@RunWith(MockitoJUnitRunner::class)
class NameViewModelTest {
    private val wizardCache: WizardCache = mock()
    private val viewModel = NameViewModel(wizardCache)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `should emit showNext as false`() = runTest {
        //given:
        val expected = false
        whenever(wizardCache.name).thenReturn(PersonName("", "", LocalDate.now()))
        //when:
        viewModel.getFromCache()
        val actual = viewModel.showNext.value
        //then:
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `should emit showNext as true`() = runTest {
        //given:
        val expected = true
        whenever(wizardCache.name).thenReturn(PersonName("Name", "Surname", LocalDate.MIN))
        //when:
        viewModel.getFromCache()
        val actual = viewModel.showNext.value
        //then:
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `should emit showAgeRestricted as false`() = runTest {
        //given:
        val expected = false
        whenever(wizardCache.name).thenReturn(PersonName("Name", "Surname",
            LocalDate.now().minusYears(18) ))
        //when:
        viewModel.getFromCache()
        val actual = viewModel.showAgeRestricted.value
        //then:
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `should emit showAgeRestricted as true`() = runTest {
        //given:
        val expected = true
        whenever(wizardCache.name).thenReturn(PersonName("Name", "Surname",
            LocalDate.now().minusYears(18).plusDays(1) ))
        //when:
        viewModel.getFromCache()
        val actual = viewModel.showAgeRestricted.value
        //then:
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `should emit showFieldsEmpty as false`() = runTest {
        //given:
        val expected = false
        whenever(wizardCache.name).thenReturn(PersonName("Name", "Surname", LocalDate.now()))
        //when:
        viewModel.getFromCache()
        val actual = viewModel.showFieldsEmpty.value
        //then:
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `should emit showFieldsEmpty as true`() = runTest {
        //given:
        val expected = true
        whenever(wizardCache.name).thenReturn(PersonName("", "", LocalDate.now()))
        //when:
        viewModel.getFromCache()
        val actual = viewModel.showFieldsEmpty.value
        //then:
        Assert.assertEquals(expected, actual)
    }
}
