package org.amber.rich.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import androidx.activity.result.contract.ActivityResultContracts

/**
 * create by colin
 * 2023/6/25
 */
class CreateFileContract : ActivityResultContracts.CreateDocument("application/text") {
    override fun createIntent(context: Context, input: String): Intent {
        return super.createIntent(context, input).putExtra(
            DocumentsContract.EXTRA_INITIAL_URI, Uri.parse(Environment.DIRECTORY_DOCUMENTS)
        )
    }
}

class OpenFileContract : ActivityResultContracts.OpenDocument() {

    override fun createIntent(context: Context, input: Array<String>): Intent {
        return super.createIntent(context, input).putExtra(
            DocumentsContract.EXTRA_INITIAL_URI, Uri.parse(Environment.DIRECTORY_DOCUMENTS)
        )
    }
}