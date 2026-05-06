package com.example.rumedcalendar.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.rumedcalendar.ui.screens.upload.ExtractedUploadItemUi
import com.example.rumedcalendar.ui.screens.upload.UploadScreen

@Composable
fun MedCalApp() {
    var extractedItems by remember {
        mutableStateOf(
            listOf(
                ExtractedUploadItemUi(
                    id = "preview_1",
                    title = "Lisinopril 10 mg",
                    details = "Take once every morning for blood pressure control."
                )
            )
        )
    }

    UploadScreen(
        extractedItems = extractedItems,
        onPickFileClick = {
            // UI-only stub; file picker logic will be connected to existing layers later.
        },
        onTakePhotoClick = {
            // UI-only stub; camera logic will be connected to existing layers later.
        },
        onConfirmItemClick = { id ->
            extractedItems = extractedItems.filterNot { it.id == id }
        }
    )
}
