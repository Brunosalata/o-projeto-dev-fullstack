<template>
  <div>
    {{ user.name }}
    {{ isAuth }}
    <NuxtLink to="/">Index</NuxtLink>
    <NuxtLink to="cadastro">Cadastro</NuxtLink>
    <button @click="logout">Sair</button>

    <router-view></router-view>
  </div>
</template>

<script>
export default {
  data() {
    return {
      user: this.$store.getters['auth/info'],
      isAuth: this.$store.getters['auth/isAuthenticated']
    }
  },
  methods: {
    logout() {
      this.$store.dispatch('auth/logout');
      this.$router.push('/login');
    }
  }
}
</script>