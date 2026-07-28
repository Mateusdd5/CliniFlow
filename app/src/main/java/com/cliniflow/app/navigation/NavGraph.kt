package com.cliniflow.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cliniflow.app.model.Usuario
import com.cliniflow.app.ui.admin.AdminHistoricoScreen
import com.cliniflow.app.ui.admin.AdminHomeScreen
import com.cliniflow.app.ui.admin.AdminUsuariosScreen
import com.cliniflow.app.ui.admin.EditarPerfilAdminScreen
import com.cliniflow.app.ui.auth.CadastroScreen
import com.cliniflow.app.ui.auth.LoginScreen
import com.cliniflow.app.ui.medico.EditarAgendaScreen
import com.cliniflow.app.ui.medico.EditarPerfilMedicoScreen
import com.cliniflow.app.ui.medico.MedicoConsultasScreen
import com.cliniflow.app.ui.medico.MedicoHomeScreen
import com.cliniflow.app.ui.paciente.BuscarMedicosScreen
import com.cliniflow.app.ui.paciente.EditarPerfilScreen
import com.cliniflow.app.ui.paciente.MinhasConsultasScreen
import com.cliniflow.app.ui.paciente.PacienteHomeScreen
import com.cliniflow.app.ui.paciente.SelecionarHorarioScreen

object Rotas {
    const val LOGIN = "login"
    const val CADASTRO = "cadastro"
    const val HOME_PACIENTE = "home_paciente"
    const val HOME_MEDICO = "home_medico"
    const val HOME_ADMIN = "home_admin"
    const val BUSCAR_MEDICOS = "buscar_medicos"
    const val SELECIONAR_HORARIO = "selecionar_horario"
    const val MINHAS_CONSULTAS = "minhas_consultas"
    const val EDITAR_PERFIL_PACIENTE = "editar_perfil_paciente"
    const val EDITAR_AGENDA = "editar_agenda"
    const val MEDICO_CONSULTAS = "medico_consultas"
    const val EDITAR_PERFIL_MEDICO = "editar_perfil_medico"
    const val ADMIN_USUARIOS = "admin_usuarios"
    const val ADMIN_HISTORICO = "admin_historico"
    const val EDITAR_PERFIL_ADMIN = "editar_perfil_admin"
}

private fun rotaHomePor(usuario: Usuario): String = when (usuario.tipo) {
    "medico" -> Rotas.HOME_MEDICO
    "admin" -> Rotas.HOME_ADMIN
    else -> Rotas.HOME_PACIENTE
}

@Composable
fun CliniFlowNavGraph(navController: NavHostController = rememberNavController()) {

    var medicoSelecionado by remember { mutableStateOf<Usuario?>(null) }

    NavHost(navController = navController, startDestination = Rotas.LOGIN) {

        composable(Rotas.LOGIN) {
            LoginScreen(
                onLoginSucesso = { usuario ->
                    navController.navigate(rotaHomePor(usuario)) { popUpTo(Rotas.LOGIN) { inclusive = true } }
                },
                onIrParaCadastro = { navController.navigate(Rotas.CADASTRO) }
            )
        }

        composable(Rotas.CADASTRO) {
            CadastroScreen(
                onCadastroSucesso = { usuario ->
                    navController.navigate(rotaHomePor(usuario)) { popUpTo(Rotas.LOGIN) { inclusive = true } }
                },
                onVoltarParaLogin = { navController.popBackStack() }
            )
        }

        composable(Rotas.HOME_PACIENTE) {
            PacienteHomeScreen(
                onNovoAgendamento = { navController.navigate(Rotas.BUSCAR_MEDICOS) },
                onMinhasConsultas = { navController.navigate(Rotas.MINHAS_CONSULTAS) },
                onEditarPerfil = { navController.navigate(Rotas.EDITAR_PERFIL_PACIENTE) },
                onLogout = { navController.navigate(Rotas.LOGIN) { popUpTo(0) { inclusive = true } } }
            )
        }

        composable(Rotas.BUSCAR_MEDICOS) {
            BuscarMedicosScreen(
                onMedicoSelecionado = { medico ->
                    medicoSelecionado = medico
                    navController.navigate(Rotas.SELECIONAR_HORARIO)
                }
            )
        }

        composable(Rotas.SELECIONAR_HORARIO) {
            medicoSelecionado?.let { medico ->
                SelecionarHorarioScreen(
                    medico = medico,
                    onVoltar = { navController.popBackStack() },
                    onConcluido = { navController.popBackStack(Rotas.HOME_PACIENTE, inclusive = false) }
                )
            }
        }

        composable(Rotas.MINHAS_CONSULTAS) { MinhasConsultasScreen() }

        composable(Rotas.EDITAR_PERFIL_PACIENTE) {
            EditarPerfilScreen(
                onLogout = { navController.navigate(Rotas.LOGIN) { popUpTo(0) { inclusive = true } } }
            )
        }

        composable(Rotas.HOME_MEDICO) {
            MedicoHomeScreen(
                onEditarAgenda = { navController.navigate(Rotas.EDITAR_AGENDA) },
                onMinhasConsultas = { navController.navigate(Rotas.MEDICO_CONSULTAS) },
                onEditarPerfil = { navController.navigate(Rotas.EDITAR_PERFIL_MEDICO) },
                onLogout = { navController.navigate(Rotas.LOGIN) { popUpTo(0) { inclusive = true } } }
            )
        }

        composable(Rotas.EDITAR_AGENDA) {
            EditarAgendaScreen(onVoltar = { navController.popBackStack() })
        }

        composable(Rotas.MEDICO_CONSULTAS) {
            MedicoConsultasScreen(onVoltar = { navController.popBackStack() })
        }

        composable(Rotas.EDITAR_PERFIL_MEDICO) {
            EditarPerfilMedicoScreen(
                onVoltar = { navController.popBackStack() },
                onLogout = { navController.navigate(Rotas.LOGIN) { popUpTo(0) { inclusive = true } } }
            )
        }

        composable(Rotas.HOME_ADMIN) {
            AdminHomeScreen(
                onVerUsuarios = { navController.navigate(Rotas.ADMIN_USUARIOS) },
                onVerHistorico = { navController.navigate(Rotas.ADMIN_HISTORICO) },
                onEditarPerfil = { navController.navigate(Rotas.EDITAR_PERFIL_ADMIN) },
                onLogout = { navController.navigate(Rotas.LOGIN) { popUpTo(0) { inclusive = true } } }
            )
        }

        composable(Rotas.ADMIN_USUARIOS) {
            AdminUsuariosScreen(onVoltar = { navController.popBackStack() })
        }

        composable(Rotas.ADMIN_HISTORICO) {
            AdminHistoricoScreen(onVoltar = { navController.popBackStack() })
        }

        composable(Rotas.EDITAR_PERFIL_ADMIN) {
            EditarPerfilAdminScreen(
                onVoltar = { navController.popBackStack() },
                onLogout = { navController.navigate(Rotas.LOGIN) { popUpTo(0) { inclusive = true } } }
            )
        }
    }
}