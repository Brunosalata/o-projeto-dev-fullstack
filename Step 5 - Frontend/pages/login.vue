<template>
  <div class="card-center">
    <v-card
      elevation="2"
      outlined
      class="card-login">

      <v-card-title>Login</v-card-title>

      <v-card-text>
        
        <v-row>
          <v-col cols="12">
            <h1>{{mensagem}}</h1>
          </v-col>
        </v-row>

        <v-row>
          <v-col cols="12">
            <label>Login:</label>
            <input v-model="user_data.login">
          </v-col>
        </v-row>

        <v-row>
          <v-col cols="12">
            <label>Senha:</label>
            <input type="password" v-model="user_data.password">
          </v-col>
        </v-row>
        
        <v-row>
          <v-col cols="12" align-items="center">
            <v-btn
              elevation="2"
              @click="login">
              Entrar
            </v-btn>
          </v-col>
        </v-row>

        <v-row>
          <v-col cols="12" align-items="center">
            <v-divider class="mx-4"></v-divider>
          </v-col>
        </v-row>

        <v-row>
          <v-col cols="12" align-items="center">
            <Geolocalizacao/>
          </v-col>
        </v-row>

      </v-card-text>

    </v-card>

  </div>
</template>

<script>
export default {
  name: 'LoginPage',
  data() {
    return {
      user_data: {
        login: "bruno",
        password: "123"
      },
      mensagem: null
    }
  },
  methods: {
    login() {
      this.$axios.post('/auth/login', this.user_data)
      .then((response) => {
        this.$store.dispatch('auth/login', response);
        this.$router.push('/');
      })
      .catch((e) => this.mensagem = e);
    }
  }
}
</script>

<style scoped>
  .card-center {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100vw;
    height: 100vh;
  }
  .card-login {
    width: 100%;
    min-width: 290px;
    max-width: 400px;
  }
</style>