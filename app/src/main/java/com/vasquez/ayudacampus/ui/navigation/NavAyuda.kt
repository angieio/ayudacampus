package com.vasquez.ayudacampus.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.platform.LocalContext
import com.vasquez.ayudacampus.data.local.AyudaDatabase
import com.vasquez.ayudacampus.data.prefs.PreferenciasStore
import com.vasquez.ayudacampus.data.repository.AyudaRepositoryRoom
import com.vasquez.ayudacampus.model.Rol
import com.vasquez.ayudacampus.ui.cliente.LoginScreen
import com.vasquez.ayudacampus.ui.cliente.MisTicketsScreen
import com.vasquez.ayudacampus.ui.cliente.NuevoTicketScreen
import com.vasquez.ayudacampus.ui.cliente.SplashScreen
import com.vasquez.ayudacampus.viewmodel.AyudaViewModelFactory
import com.vasquez.ayudacampus.viewmodel.MisTicketsViewModel
import com.vasquez.ayudacampus.viewmodel.NuevoTicketViewModel
import com.vasquez.ayudacampus.viewmodel.SesionViewModel
import kotlinx.coroutines.flow.first

@Composable
fun NavAyuda(preferencias: PreferenciasStore) {
    val context = LocalContext.current
    val navController = rememberNavController()
    val usuarioIdGuardado by preferencias.usuarioId.collectAsState(initial = 0)

    val repo = remember(context) {
        AyudaRepositoryRoom(AyudaDatabase.obtener(context))
    }

    val factory = remember(repo, preferencias) {
        AyudaViewModelFactory(repo, preferencias)
    }

    val vm: SesionViewModel = viewModel(factory = factory)
    val usuario by vm.usuarioActual.collectAsState()

    LaunchedEffect(usuario?.id) {
        val actual = usuario ?: return@LaunchedEffect

        val destino = if (actual.rol == Rol.CLIENTE.name) {
            Rutas.MIS_TICKETS
        } else {
            Rutas.PANEL
        }

        navController.navigate(destino) {
            popUpTo(Rutas.LOGIN) { inclusive = true }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Rutas.SPLASH
    ) {
        composable(Rutas.SPLASH) {
            LaunchedEffect(Unit) {
                val perfil = preferencias.perfilActivo.first()

                val destino = when (perfil) {
                    Rol.CLIENTE.name -> Rutas.MIS_TICKETS
                    Rol.ADMIN.name -> Rutas.PANEL
                    else -> Rutas.LOGIN
                }

                navController.navigate(destino) {
                    popUpTo(Rutas.SPLASH) { inclusive = true }
                }
            }

            SplashScreen()
        }

        composable(Rutas.LOGIN) {
            LoginScreen(vm)
        }

        composable(Rutas.MIS_TICKETS) {
            val idActual = usuario?.id ?: usuarioIdGuardado

            if (idActual > 0) {
                val factoryTickets = remember(repo, preferencias, idActual) {
                    AyudaViewModelFactory(repo, preferencias, idActual)
                }

                val misTicketsVm: MisTicketsViewModel = viewModel(
                    key = "mis_tickets_$idActual",
                    factory = factoryTickets
                )

                MisTicketsScreen(
                    vm = misTicketsVm,
                    nombreUsuario = usuario?.nombre ?: "Estudiante",
                    onTicketClick = { ticketId ->
                        navController.navigate("${Rutas.DETALLE_TICKET}/$ticketId")
                    },
                    onNuevoClick = {
                        navController.navigate(Rutas.NUEVO_TICKET)
                    },
                    onPerfilClick = {
                        navController.navigate(Rutas.PERFIL)
                    }
                )
            } else {
                Text("Cargando tus tickets...")
            }
        }

        composable(Rutas.PANEL) {
            Text("Panel de soporte")
        }

        composable(Rutas.NUEVO_TICKET) {
            val idActual = usuario?.id ?: usuarioIdGuardado

            if (idActual > 0) {
                val factoryNuevo = remember(repo, preferencias, idActual) {
                    AyudaViewModelFactory(repo, preferencias, idActual)
                }

                val nuevoTicketVm: NuevoTicketViewModel = viewModel(
                    key = "nuevo_ticket_$idActual",
                    factory = factoryNuevo
                )

                NuevoTicketScreen(
                    vm = nuevoTicketVm,
                    onVolver = {
                        navController.popBackStack()
                    },
                    onTicketCreado = {
                        navController.popBackStack()
                    },
                    onTicketsClick = {
                        navController.popBackStack(Rutas.MIS_TICKETS, false)
                    },
                    onPerfilClick = {
                        navController.navigate(Rutas.PERFIL)
                    }
                )
            } else {
                Text("Cargando usuario...")
            }
        }

        composable("${Rutas.DETALLE_TICKET}/{ticketId}") { entrada ->
            val ticketId = entrada.arguments?.getString("ticketId")
            Text("Detalle del ticket $ticketId: valor por defect")
        }

        composable(Rutas.PERFIL) {
            Text("Perfil")
        }
    }
}