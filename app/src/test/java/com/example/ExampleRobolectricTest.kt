package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.ModsRepository
import com.example.data.PreferencesManager
import org.junit.Assert.assertEquals
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
    assertEquals("SwordMods", appName)
  }

  @Test
  fun `verify Innersloth 24-hour session persistence`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val prefs = PreferencesManager(context)
    val session = prefs.getInnerslothSession()

    assertTrue(session.isLoggedIn)
    assertTrue(session.isSessionValid)
    // Verifica que a sessão dura pelo menos 23 horas no momento da criação
    assertTrue(session.remainingHours >= 23L)
  }

  @Test
  fun `verify real storage replay file generation and scanning`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val prefs = PreferencesManager(context)
    val repo = ModsRepository(context, prefs)

    val dir = repo.getReplaysDirectory()
    assertTrue(dir.exists())

    val created = repo.createSampleRealReplay("The Skeld")
    assertTrue(created.fileSizeBytes > 0)

    val replays = repo.getRealReplays()
    assertTrue(replays.isNotEmpty())

    val deleted = repo.deleteReplayFile(created.filePath)
    assertTrue(deleted)
  }
}
