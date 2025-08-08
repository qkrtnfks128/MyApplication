package com.example.myapplication.page.admin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.manager.AdminManager
import com.example.myapplication.manager.SelectedOrgStore
import com.example.myapplication.model.AdminOrg
import com.example.myapplication.navigation.Screen
import com.example.myapplication.ui.theme.MyApplicationTheme

@Composable
fun AdminOrgSelectScreen(
    navController: NavController,
) {
    val sessionFlow = AdminManager.observeAdminSession()
    val sessionState = sessionFlow?.collectAsStateWithLifecycle(initialValue = null)
    val orgs: List<AdminOrg> = (sessionState?.value?.adminOrgs).orEmpty()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "기관 선택",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(orgs) { item ->
                AdminOrgRow(
                    org = item,
                    onClick = {
                        SelectedOrgStore.saveSelected(item)
                        // 모든 페이지를 다 지우고 메인으로 이동
                        navController.navigate(Screen.Main.route) {
                            popUpTo(0) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun AdminOrgRow(
    org: AdminOrg,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = org.orgName, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "학급 수: ${org.classCount}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AdminOrgSelectPreview() {
    MyApplicationTheme {
        AdminOrgSelectScreen(rememberNavController())
    }
}


