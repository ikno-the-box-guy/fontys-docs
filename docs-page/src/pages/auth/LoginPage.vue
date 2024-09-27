<script setup lang="ts">
import axios from "axios";
import router from "../../main.ts";
import {ref} from "vue";
import token from "../../utils/localToken.ts";
import user from "../../utils/localUser.ts";

const loginError = ref(null);

const login = (event: SubmitEvent) => {
  event.stopPropagation();
  event.preventDefault();
  
  const email = (event.target as HTMLFormElement).email.value;
  const password = (event.target as HTMLFormElement).password.value;
  
  // Send the email and password to the server
  axios.post(import.meta.env.VITE_AUTH_API_URL + "/auth/login", {
    email,
    password
  }).then((response) => {
    token.value = response.data.token;
    user.value = {
      name: response.data.displayName,
      email: response.data.email,
      root: response.data.rootDirectoryId
    }
    
    // Redirect the user to the home page
    router.push({name: "home"});
  }).catch((error) => {
    console.error(error);
    loginError.value = error
  });
}
</script>

<template>
  <div class="max-w-2xl mx-auto">
    <div class="center bg-white shadow-md border border-gray-200 rounded-lg max-w-sm p-4 sm:p-6 lg:p-8 dark:bg-gray-800 dark:border-gray-700 w-1/2">
      <form class="space-y-6" @submit="login">
        <h3 class="text-xl font-medium text-gray-900 dark:text-white">Sign in to our platform</h3>
        <div>
          <label for="email" class="text-sm font-medium text-gray-900 block mb-2 dark:text-gray-300">Your email</label>
          <input type="email" name="email" id="email" class="bg-gray-50 border border-gray-300 text-gray-900 sm:text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-600 dark:border-gray-500 dark:placeholder-gray-400 dark:text-white" placeholder="name@company.com" required="">
        </div>
        <div>
          <label for="password" class="text-sm font-medium text-gray-900 block mb-2 dark:text-gray-300">Your password</label>
          <input type="password" name="password" id="password" placeholder="••••••••" class="bg-gray-50 border border-gray-300 text-gray-900 sm:text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-600 dark:border-gray-500 dark:placeholder-gray-400 dark:text-white" required="">
        </div>
        <button type="submit" class="w-full text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800">Login to your account</button>
        <div class="text-sm font-medium text-gray-500 dark:text-gray-300">
          Not registered? <RouterLink :to="{name: 'register'}" class="text-blue-700 hover:underline dark:text-blue-500">Create
          account</RouterLink>
        </div>
      </form>
      
      <div v-if="loginError" class="flex mt-8 items-center p-4 mb-4 text-sm text-red-800 border border-red-300 rounded-lg bg-red-50 dark:bg-gray-800 dark:text-red-400 dark:border-red-800" role="alert">
        <svg class="flex-shrink-0 inline w-4 h-4 me-3" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="currentColor" viewBox="0 0 20 20">
          <path d="M10 .5a9.5 9.5 0 1 0 9.5 9.5A9.51 9.51 0 0 0 10 .5ZM9.5 4a1.5 1.5 0 1 1 0 3 1.5 1.5 0 0 1 0-3ZM12 15H8a1 1 0 0 1 0-2h1v-3H8a1 1 0 0 1 0-2h2a1 1 0 0 1 1 1v4h1a1 1 0 0 1 0 2Z"/>
        </svg>
        <span class="sr-only">Info</span>
        <div>
          <span class="font-medium">Login error!</span> Incorrect credentials
        </div>
      </div>
    </div>
    
  </div>
</template>

<style scoped>

</style>