package com.example

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.AppRepository
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.AprovaViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("APROVA", appName)
  }

  @Test
  fun `test repository student name persistence without auth`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val repository = AppRepository(context)
    repository.saveStudentName("Manuel Silva")
    assertEquals("Manuel Silva", repository.getStudentName())
    assertTrue(repository.hasStudentName())
  }

  @Test
  fun `test repository subjects and content availability`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val repository = AppRepository(context)
    val subjects = repository.getAllSubjects()
    assertTrue(subjects.size >= 12)

    val matSubject = subjects.find { it.id == "mat" }
    assertNotNull(matSubject)
    assertEquals("Matemática", matSubject?.name)

    val questions = repository.getQuestions()
    assertTrue(questions.isNotEmpty())
  }

  @Test
  fun `test viewmodel student onboarding flow`() {
    val app = ApplicationProvider.getApplicationContext<Application>()
    val viewModel = AprovaViewModel(app)
    viewModel.submitStudentName("Ana Santos")

    assertEquals("Ana Santos", viewModel.studentName.value)
    assertEquals(AppScreen.MainNav, viewModel.currentScreen.value)
  }
}

