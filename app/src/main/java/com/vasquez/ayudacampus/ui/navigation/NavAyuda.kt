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
import com.vasquez.ayudacampus.ui.cliente.DetalleTicketScreen
import com.vasquez.ayudacampus.ui.cliente.LoginScreen
import com.vasquez.ayudacampus.ui.cliente.MisTicketsScreen
import com.vasquez.ayudacampus.ui.cliente.NuevoTicketScreen
import com.vasquez.ayudacampus.ui.cliente.SplashScreen
import com.vasquez.ayudacampus.ui.soporte.BandejaScreen
import com.vasquez.ayudacampus.ui.soporte.AtenderTicketScreen
import com.vasquez.ayudacampus.ui.soporte.CategoriasScreen
import com.vasquez.ayudacampus.ui.soporte.FormularioCategoriaScreen
import com.vasquez.ayudacampus.ui.soporte.PanelScreen
import com.vasquez.ayudacampus.viewmodel.AyudaViewModelFactory
import com.vasquez.ayudacampus.viewmodel.BandejaViewModel
import com.vasquez.ayudacampus.viewmodel.AtenderViewModel
import com.vasquez.ayudacampus.viewmodel.CategoriasViewModel
import com.vasquez.ayudacampus.viewmodel.MisTicketsViewModel
import com.vasquez.ayudacampus.viewmodel.NuevoTicketViewModel
import com.vasquez.ayudacampus.viewmodel.PanelViewModel
import com.vasquez.ayudacampus.viewmodel.SesionViewModel
import com.vasquez.ayudacampus.viewmodel.TicketDetalleViewModel
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
            val panelVm: PanelViewModel = viewModel(factory = factory)

            PanelScreen(
                vm = panelVm,
                onTicketsClick = { navController.navigate(Rutas.BANDEJA) },
                onCategoriasClick = { navController.navigate(Rutas.CATEGORIAS) },
                onAjustesClick = { navController.navigate(Rutas.AJUSTES) },
                onVerAltasClick = { navController.navigate(Rutas.BANDEJA) }
            )
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
            val ticketId = entrada.arguments
                ?.getString("ticketId")
                ?.toIntOrNull()

            if (ticketId != null) {
                val factoryDetalle = remember(repo, preferencias, ticketId) {
                    AyudaViewModelFactory(
                        repo = repo,
                        preferencias = preferencias,
                        ticketId = ticketId
                    )
                }

                val detalleVm: TicketDetalleViewModel = viewModel(
                    key = "detalle_ticket_$ticketId",
                    factory = factoryDetalle
                )

                DetalleTicketScreen(
                    vm = detalleVm,
                    onVolver = { navController.popBackStack() },
                    onTicketsClick = {
                        navController.popBackStack(Rutas.MIS_TICKETS, false)
                    },
                    onNuevoClick = {
                        navController.navigate(Rutas.NUEVO_TICKET)
                    },
                    onPerfilClick = {
                        navController.navigate(Rutas.PERFIL)
                    }
                )
            } else {
                Text("No se encontró el identificador del ticket.")
            }
        }

        composable(Rutas.PERFIL) {
            Text("Perfil")
        }

        composable(Rutas.BANDEJA) {
            val bandejaVm: BandejaViewModel = viewModel(factory = factory)

            BandejaScreen(
                vm = bandejaVm,
                onPanelClick = { navController.navigate(Rutas.PANEL) },
                onCategoriasClick = { navController.navigate(Rutas.CATEGORIAS) },
                onAjustesClick = { navController.navigate(Rutas.AJUSTES) },
                onTicketClick = { ticketId ->
                    navController.navigate("${Rutas.ATENDER_TICKET}/$ticketId")
                }
            )
        }

        composable(Rutas.CATEGORIAS) {
            val categoriasVm: CategoriasViewModel = viewModel(factory = factory)
            CategoriasScreen(
                vm = categoriasVm,
                onPanelClick = { navController.navigate(Rutas.PANEL) },
                onTicketsClick = { navController.navigate(Rutas.BANDEJA) },
                onAjustesClick = { navController.navigate(Rutas.AJUSTES) },
                onNuevaClick = { navController.navigate(Rutas.FORMULARIO_CATEGORIA) },
                onEditarClick = { id -> navController.navigate("${Rutas.FORMULARIO_CATEGORIA}/$id") }
            )
        }

        composable(Rutas.FORMULARIO_CATEGORIA) {
            val categoriasVm: CategoriasViewModel = viewModel(factory = factory)
            FormularioCategoriaScreen(categoriasVm, categoriaId = null, onVolver = { navController.popBackStack() })
        }

        composable("${Rutas.FORMULARIO_CATEGORIA}/{categoriaId}") { entrada ->
            val categoriaId = entrada.arguments?.getString("categoriaId")?.toIntOrNull()
            val categoriasVm: CategoriasViewModel = viewModel(factory = factory)
            FormularioCategoriaScreen(categoriasVm, categoriaId = categoriaId, onVolver = { navController.popBackStack() })
        }

        composable(Rutas.AJUSTES) {
            Text("Ajustes")
        }

        composable("${Rutas.ATENDER_TICKET}/{ticketId}") { entrada ->
            val ticketId = entrada.arguments?.getString("ticketId")?.toIntOrNull()
            val soporteId = usuario?.id ?: usuarioIdGuardado
            if (ticketId != null && soporteId > 0) {
                val factoryAtender = remember(repo, preferencias, ticketId, soporteId) {
                    AyudaViewModelFactory(repo, preferencias, usuarioId = soporteId, ticketId = ticketId)
                }
                val atenderVm: AtenderViewModel = viewModel(
                    key = "atender_$ticketId",
                    factory = factoryAtender
                )
                AtenderTicketScreen(
                    vm = atenderVm,
                    onVolver = { navController.popBackStack() },
                    onPanelClick = { navController.navigate(Rutas.PANEL) },
                    onTicketsClick = { navController.navigate(Rutas.BANDEJA) },
                    onCategoriasClick = { navController.navigate(Rutas.CATEGORIAS) },
                    onAjustesClick = { navController.navigate(Rutas.AJUSTES) }
                )
            } else {
                Text("Cargando ticket y usuario de Soporte…")
            }
        }
    }
}
