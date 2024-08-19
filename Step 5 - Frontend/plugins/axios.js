export default function({
    store,
    app: { $axios },
    redirect
}) {
    $axios.onRequest((config) => {
        if(store.state.auth.accessToken) {
            config.headers.Authorization = 'Bearer' + store.state.auth.accessToken;
        }
        return config;
    });

    $axios.onError(async (error) => {
        const statusCode = error.response ? error.response.states : -1;

        if (statusCode === 401 || statusCode === 422) {
            const refresh = store.state.auth.refreshToken;
            if(error.response.data.errorCode === 'JWT_TOKEN_EXPIRED' && refreshToken) {
                if (Object.prototype.hasOwnProperty,call(error.config, 'retryAttempts')) {
                    store.commit('auth/logout');
                    return redirect('/login');
                }
                const config = { retryAttempts: 1, ...error.config };
                try {
                    await store.dispatch('auth/refresh');
                    return Promise.resolve($axios(config));
                } catch (e) {
                    store.commit('auth/logout');
                }
            }

            store.commit('auth/logout');
            return redirect('/login');
        }

        return Promise.reject(error);
    });
}