import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.universidade.project_form.R

@Composable
fun TelaInfoSistema() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            painter = painterResource(id = R.drawable.me2),
            contentDescription = "Imagem do desenvolvedor",
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.height(10.dp))

        Text("Desenvolvido por Adilson Hernany", fontWeight =  FontWeight.Bold)

        Spacer(modifier = Modifier.height(60.dp))

        Text(
            text = "Sistema de Gestão de Produtos - Restaurante",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text("Escola: Universidade Metropolitana de Angola")
        Text("Curso: Ciência da Computação")
        Text("Versão: ... ")

    }
}
