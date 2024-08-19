<!-- Please remove this file from your project -->
<template>
  <div>
      <div>
          <h1>{{ nomeFormatado }}</h1>
      </div>
      <div 
          v-for="v in vetor"
          :key="v"
          v-show="v != 3">
          {{ v }}
      </div>
  </div>
</template>

<script>
export default {
  name: 'NuxtTutorial',
  props: ['nome'],
  data() {
  return {
      nomeFormatado: 'NOME: ' + this.nome,
      vetor: [1, 2, 3, 4, 5]
  }
  }
}
</script>
