package io.github.droidkaigi.confsched.testing.previewtester

import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onRoot
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.github.takahirom.roborazzi.ComposePreviewTester
import com.github.takahirom.roborazzi.ExperimentalRoborazziApi
import com.github.takahirom.roborazzi.RoborazziActivity
import com.github.takahirom.roborazzi.captureRoboImage
import com.github.takahirom.roborazzi.registerRoborazziActivityToRobolectricIfNeeded
import io.github.droidkaigi.confsched.testing.rule.CoilRule
import org.junit.rules.RuleChain
import org.junit.rules.TestWatcher
import sergio.sastre.composable.preview.scanner.jvm.common.CommonComposablePreviewScanner
import sergio.sastre.composable.preview.scanner.jvm.common.CommonPreviewInfo

@Suppress("UNUSED")
@OptIn(ExperimentalRoborazziApi::class)
class DroidKaigiKmpPreviewTester : ComposePreviewTester<ComposePreviewTester.TestParameter.JUnit4TestParameter<CommonPreviewInfo>> {
    override fun test(testParameter: ComposePreviewTester.TestParameter.JUnit4TestParameter<CommonPreviewInfo>) {
        val preview = testParameter.preview
        val screenshotNameSuffix = preview.previewIndex?.let { "_" + preview.previewIndex }.orEmpty()
        testParameter.composeTestRule.setContent {
            ProvideAndroidContextToComposeResource()
            preview()
        }
        testParameter.composeTestRule
            .onRoot()
            .captureRoboImage(preview.methodName + screenshotNameSuffix + ".png")
    }

    override fun testParameters(): List<ComposePreviewTester.TestParameter.JUnit4TestParameter<CommonPreviewInfo>> {
        val options = options()
        return CommonComposablePreviewScanner()
            .scanPackageTrees(*options.scanOptions.packages.toTypedArray())
            .includePrivatePreviews()
            .getPreviews()
            .map {
                ComposePreviewTester.TestParameter.JUnit4TestParameter(
                    composeTestRuleFactory = (options.testLifecycleOptions as ComposePreviewTester.Options.JUnit4TestLifecycleOptions).composeRuleFactory,
                    preview = it,
                )
            }
    }

    override fun options(): ComposePreviewTester.Options = super.options().copy(
        testLifecycleOptions = ComposePreviewTester.Options.JUnit4TestLifecycleOptions(
            composeRuleFactory = {
                @Suppress("UNCHECKED_CAST")
                createAndroidComposeRule<RoborazziActivity>() as AndroidComposeTestRule<ActivityScenarioRule<out androidx.activity.ComponentActivity>, *>
            },
            testRuleFactory = { composeTestRule ->
                RuleChain.outerRule(
                    object : TestWatcher() {
                        override fun starting(description: org.junit.runner.Description?) {
                            super.starting(description)
                            registerRoborazziActivityToRobolectricIfNeeded()
                        }
                    },
                )
                    .around(composeTestRule)
                    .around(CoilRule())
            },
        ),
    )
}
