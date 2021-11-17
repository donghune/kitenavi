package com.github.donghune.kitenavi.view.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.github.donghune.kitenavi.model.network.response.Document
import com.github.donghune.kitenavi.view.component.DoubleLineCard
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AddressSearchActivity : ComponentActivity() {

    private val viewModel by lazy { AddressSearchViewModel.Factory(this).build() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val groupId = intent.getIntExtra(EXTRA_GROUP_ID, 0)

            val textField = remember { mutableStateOf("낙성대역 18길") }
            val searchResult = remember { mutableStateOf(listOf<Document>()) }

            lifecycleScope.launch {
                viewModel.queryResult.collect {
                    searchResult.value = it
                }
            }

            Scaffold(
                topBar = {
                    TopAppBar {
                        TextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = textField.value,
                            onValueChange = { value ->
                                textField.value = value
                                viewModel.search(value)
                            },
                            label = {
                                Text(text = "Search")
                            },
                            leadingIcon = {
                                Icon(Icons.Filled.Search, "")
                            },
                            singleLine = true
                        )
                    }
                }
            ) {
                Column(
                    modifier = Modifier.verticalScroll(ScrollState(0))
                ) {
                    searchResult.value.forEach {
                        AddressSearchResponseCard(document = it) {
                            viewModel.insertAddress(it.address_name, it.y, it.x, groupId)
                            Toast.makeText(
                                this@AddressSearchActivity,
                                "주소가 추가되었습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun AddressSearchResponseCard(
        document: Document,
        selectButtonClick: () -> Unit
    ) {
        DoubleLineCard(
            title = document.address_name,
            text = document.address_type,
            buttonName = "선택",
            buttonClick = selectButtonClick
        )
    }

    companion object {
        private const val EXTRA_GROUP_ID = "EXTRA_GROUP_ID"
        fun startActivity(context: Context, groupId: Int) {
            context.startActivity(
                Intent(context, AddressSearchActivity::class.java).apply {
                    putExtra(EXTRA_GROUP_ID, groupId)
                }
            )
        }
    }
}
