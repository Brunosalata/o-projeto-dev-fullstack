export const state = () => ({
    accessToken: null,
    refreshToken:null,
    user: null
});

export const getters = {
    isAuthenticated(state) {
        return !state.accessToken;
    },
    info(state) {
        return state.user;
    }
};

export const mutations = {
    setToken(state, token, refreshToken) {
        state.accessToken = token;
        if(refreshToken){
            state.refreshToken = refreshToken;
        }
    },
    setUser(state, user) {
        state.user = user;
    },
    logout(state) {
        state.accessToken = null;
        state.refreshToken = null;
        state.user = null;
    }
};

export const actions = {
    async login({ commit, dispatch }, req) {
        let data = req.object || req.data;
        let user = {
            login: data.login,
            name: data.name
        };
        let token = data.token;

        commit('setToken', token, token);
        commit('setUser', user);
    }
};