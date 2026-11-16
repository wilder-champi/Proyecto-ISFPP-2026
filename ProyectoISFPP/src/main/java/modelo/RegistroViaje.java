package modelo;

import java.time.LocalDateTime;

public class RegistroViaje {
		private EstadoViaje estadoViaje;
		private LocalDateTime fechaHora;

		public RegistroViaje(EstadoViaje estadoViaje, LocalDateTime fechaHora) {
			this.estadoViaje = estadoViaje;
			this.fechaHora = fechaHora;
		}

		public EstadoViaje getEstadoViaje() {
			return estadoViaje;
		}

		public LocalDateTime getFechaHora() {
			return fechaHora;
		}

}
