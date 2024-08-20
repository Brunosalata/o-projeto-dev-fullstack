<template>
  <div>
    <div v-if="coords">
      <h1>Geolocalização</h1>
      <p>Localização atual:</p>
      <p>Latitude: {{ coords.latitude }}</p>
      <p>Longitude: {{ coords.longitude }}</p>
    </div>
    <div v-if="erro">
      {{ erro }}
    </div>
  </div>
</template>

<script>
// Essa forma de importação define a aplicação do plugin
import { Geolocation } from '@capacitor/geolocation'; 
export default {
  data() {
    return {
      coords: null,
      erro: null
    }
  },
  methods: {
    getCurrentPosition() {
      Geolocation.getCurrentPosition()
      .then((res) => {
        this.coords = res.coords;
        console.log(res);
      })
      .catch((e) => {
        this.erro = e;
      })
    }
  },
  mounted() {
    this.getCurrentPosition();
  }
}
</script>