<template>
  <div>
    <v-card elevation="2" outlined class="card-login">

      <v-card-title>Games</v-card-title>

      <v-card-text>
        <table v-if="product.length > 0">
          <tr>
            <!--<th align-self="left">{{$i18n.t('Nome')}}</th>-->
            <!---<th align-self="left">{{$i18n.t('Descricao')}}</th>-->
            <th align-self="left">Nome</th>
            <th align-self="left">Descricao</th>
            <th></th>
            <th></th>
          </tr>
          <tr v-for="produto, i in product" :key="produto.id">
            <td>{{ produto.name }}</td>
            <td>{{ produto.description }}</td>
            <td>
              <v-btn @click="edita(produto.id)">Editar</v-btn>
            </td>
            <td>
              <v-btn @click="remove(produto.id, i)">Remover</v-btn>
            </td>
          </tr>
        </table>
      </v-card-text>
    </v-card>
  </div>
</template>

<script>
export default {
  name: 'CadastroPage',
  layout: 'MainPage',
  middleware: 'authenticated',
  data() {
    return {
      product: []
    }
  },
  methods: {
    load() {
      this.$axios.get('http://localhost:8081/product')
        .then((resposta) => {
          this.product = resposta.data;
        })
        .catch((e) => {
          alert(e);
        });
    },
    edita(id) {
      this.$router.push('produto/edicao?id=' + id)
    },
    remove(id, index) {
      this.$axios.delete('http://localhost:8081/product/' + id)
        .then((res) => {
          this.product.splice(index, 1);
        })
    }
  },
  created() {
    this.load();
  }
}
</script>
