package com.github.donghune.kitenavi.view.address

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.lifecycle.lifecycleScope
import com.github.donghune.kitenavi.R
import com.github.donghune.kitenavi.model.local.Address
import com.github.donghune.kitenavi.view.component.DoubleLineCard
import com.github.donghune.kitenavi.view.search.AddressSearchActivity
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AddressActivity : ComponentActivity() {

    private val viewModel by lazy { AddressViewModel.Factory(this).build() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val groupId = intent.getIntExtra(EXTRA_GROUP_ID, 0)
            val addressList = remember { mutableStateOf(listOf<Address>()) }

            lifecycleScope.launch {
                viewModel.fetchAddressList(groupId)
                viewModel.addressList.collect {
                    addressList.value = it
                }
            }

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(text = "주소 관리") }
                    )
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            AddressSearchActivity.startActivity(this@AddressActivity, groupId)
                        }
                    ) {
                        Image(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_add_circle_24),
                            contentDescription = ""
                        )
                    }
                }
            ) {
                Column {
                    addressList.value.forEach {
                        AddressCard(address = it) {
                            viewModel.removeAddress(it)
                            viewModel.fetchAddressList(groupId)
                            Toast.makeText(
                                this@AddressActivity,
                                "주소를 삭제하였습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val groupId = intent.getIntExtra(EXTRA_GROUP_ID, 0)
        viewModel.fetchAddressList(groupId)
    }

    @Composable
    fun AddressCard(address: Address, deleteButtonClick: () -> Unit) {
        DoubleLineCard(
            title = address.id.toString(),
            text = address.name,
            buttonName = "삭제",
            buttonClick = deleteButtonClick
        )
    }

    companion object {
        private const val EXTRA_GROUP_ID = "EXTRA_GROUP_ID"
        fun startActivity(context: Context, groupId: Int) {
            context.startActivity(
                Intent(context, AddressActivity::class.java).apply {
                    putExtra(EXTRA_GROUP_ID, groupId)
                }
            )
        }
    }
}
