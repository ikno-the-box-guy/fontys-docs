import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import './index.css'


import {createRouter, createWebHistory} from 'vue-router'

import HomePage from "./pages/HomePage.vue";
import LoginPage from "./pages/auth/LoginPage.vue";
import RegisterPage from "./pages/auth/RegisterPage.vue";
import user from "./utils/localUser.ts";
import Explorer from "./pages/explorer/Explorer.vue";

const filterPage = (to, from, next) => {
    if (user.value) {
        next()
    } else {
        next('/login')
    }
}

const routes = [
    { 
        name: 'home', 
        path: '/', 
        component: HomePage,
        beforeEnter: (to, from, next) => filterPage(to, from, next)
    },
    {
        name: 'explorer',
        path: '/explorer/:directory',
        component: Explorer
    },
    { name: 'login', path: '/login', component: LoginPage },
    { name: 'register', path: "/register", component: RegisterPage}
]

const router = createRouter({
    history: createWebHistory(),
    routes,
})


createApp(App).use(router).mount('#app')

export default router
