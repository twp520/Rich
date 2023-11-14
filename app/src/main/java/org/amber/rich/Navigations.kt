package org.amber.rich

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.amber.rich.connect.ConnectRoute
import org.amber.rich.home.HomeRoute
import org.amber.rich.product.CreateProductRoute
import org.amber.rich.purchase.PurchaseRoute

/**
 * create by colin
 * 2023/4/28
 */

object Destinations {
    const val CONNECT_ROUTE = "welcome"
    const val HOME_ROUTE = "home"
    const val PURCHASE_LIST = "purchase_list"
    const val CREATE_PRODUCT = "create_product/{product_code}"
    const val NAV_NULL_PATH = "NULL"
}


@Composable
fun RichNavHost(
    navigationController: NavHostController = rememberNavController(),
    app: Application
) {

    NavHost(
        navController = navigationController,
        startDestination = Destinations.HOME_ROUTE
    ) {
        composable(Destinations.CONNECT_ROUTE) {
            ConnectRoute()
        }
        composable(Destinations.HOME_ROUTE) {
            HomeRoute(
                app,
                navToPurchaseList = {
                    navigationController.navigate(Destinations.PURCHASE_LIST)
                }, navToCreateProduct = {
                    val destination = "create_product/$it"
                    navigationController.navigate(destination)
                })
        }

        composable(
            Destinations.CREATE_PRODUCT,
            arguments = listOf(navArgument("product_code") {
                type = NavType.StringType
                nullable = true
            })
        ) {
            val code = it.arguments?.getString("product_code")
            CreateProductRoute(code, back = {
                navigationController.popBackStack()
            })
        }

        composable(Destinations.PURCHASE_LIST) {
            PurchaseRoute {
                navigationController.popBackStack()
            }
        }
    }
}