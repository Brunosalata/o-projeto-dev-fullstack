<template>
    <div>
        <v-card elevation="2" outlined class="card-login">

            <v-card-title>Edição</v-card-title>

            <v-card-text>
                <v-row>
                    <v-col cols="12">
                        <label>Nome:</label>
                        <input v-model="nome">
                    </v-col>
                </v-row>

                <v-row>
                    <v-col cols="12">
                        <label>Descrição:</label>
                        <input v-model="descricao">
                    </v-col>
                </v-row>

                <v-row>
                    <v-col cols="12" align-self="center">
                        <v-btn elevation="2" @click="salva" :disabled="invalido">
                            Salvar
                        </v-btn>
                    </v-col>
                </v-row>
            </v-card-text>

        </v-card>
        {{ mensagem }}
    </div>
</template>

<script>
export default {
    layout: 'MainPage',
    middleware: 'authenticated',
    data() {
        return {
            id: this.$route.query.id,
            nome: null,
            descricao: null,
            mensagem: null
        }
    },
    computed: {
        invalido() {
            if (this.nome == null
                || this.nome == ''
                || this.descricao == null
                || this.descricao == '') {
                return true;
            }
            return false;

        }
    },
    methods: {
        load() {
            this.$axios.get('http://localhost:8081/product/' + this.id)
                .then((resposta) => {
                    this.nome = resposta.data.name;
                    this.descricao = resposta.data.description;
                })
                .catch((e) => {
                    alert(e);
                });
        },
        salva() {
            this.$axios.post('http://localhost:8081/product',
                {
                    id: this.id,
                    name: this.nome,
                    description: this.descricao
                }
            )
                .then((res) => {
                    this.mensagem = 'Salvo com sucesso!';
                })
                .catch((e) => {
                    alert(e);
                });
        }
    },
    mounted() {
        this.load();
    }
}
</script>

<style scoped></style>