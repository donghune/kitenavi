package com.github.donghune.presenter.address

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.lifecycle.lifecycleScope
import com.github.donghune.domain.entity.Address
import com.github.donghune.presenter.R
import com.github.donghune.presenter.component.DoubleLineCard
import com.github.donghune.presenter.search.AddressSearchActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddressActivity : ComponentActivity() {

    private val viewModel: AddressViewModel by viewModels()

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
                Column(
                    modifier = Modifier.verticalScroll(ScrollState(0))
                ) {
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
