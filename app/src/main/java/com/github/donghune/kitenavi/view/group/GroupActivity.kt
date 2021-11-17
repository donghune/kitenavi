package com.github.donghune.kitenavi.view.group

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.lifecycle.lifecycleScope
import com.github.donghune.kitenavi.R
import com.github.donghune.kitenavi.model.local.Group
import com.github.donghune.kitenavi.view.address.AddressActivity
import com.github.donghune.kitenavi.view.component.DoubleLineCard
import kotlinx.coroutines.flow.collect

class GroupActivity : ComponentActivity() {

    private val viewModel by lazy { GroupViewModel.Factory(applicationContext).build() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val openDialog = rememberSaveable { mutableStateOf(false) }
            val groupList by viewModel.groupList.collectAsState()

            lifecycleScope.launchWhenResumed {
                viewModel.loadState.collect {
                    Log.d(TAG, "onCreate: it=[$it]")
                }
                viewModel.loadState.collect {
                    Log.d(TAG, "onCreate2: it=[$it]")
                }
            }

            viewModel.fetchGroupList()

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(text = "그룹 관리") }
                    )
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            openDialog.value = true
                        }
                    ) {
                        Image(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_add_circle_24),
                            contentDescription = ""
                        )
                    }
                }
            ) {
                GroupList(groupList)

                if (openDialog.value) {
                    GroupInsertDialog(openDialog)
                }
            }
        }
    }

    @Composable
    fun GroupList(
        groupList: List<Group>
    ) {
        Column(
            modifier = Modifier.verticalScroll(ScrollState(0))
        ) {
            groupList.forEach {
                GroupCard(group = it)
            }
        }
    }

    @Composable
    fun GroupCard(group: Group) {
        DoubleLineCard(
            title = group.id.toString(),
            text = group.name,
            onClick = {
                AddressActivity.startActivity(this@GroupActivity, group.id)
            },
            buttonName = "삭제",
            buttonClick = {
                viewModel.removeGroup(group)
                viewModel.fetchGroupList()
                Toast.makeText(
                    this@GroupActivity,
                    "그룹을 삭제하였습니다.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
    }

    @Composable
    fun GroupInsertDialog(
        dialogVisible: MutableState<Boolean>
    ) {
        val input = remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = {
                dialogVisible.value = false
            },
            title = {
                Text(text = "그룹이름을 입력해주세요")
            },
            text = {
                TextField(
                    value = input.value,
                    onValueChange = { value -> input.value = value }
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.insertGroup(input.value)
                        viewModel.fetchGroupList()
                        dialogVisible.value = false
                    }
                ) {
                    Text(text = "확인")
                }
            }
        )
    }

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, GroupActivity::class.java))
        }

        private val TAG = "GroupActivity"
    }
}
