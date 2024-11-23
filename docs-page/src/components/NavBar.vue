<script setup lang="ts">
import user from "../utils/localUser.ts";
import UserPopper from "./UserPopper.vue";
import {useRoute} from "vue-router";
import {computed} from "vue";

const route = useRoute();

const isActive = (path: string) => {
  return computed(() => route.path === path);
};

const roughMatch = (path: string) => {
  return computed(() => route.path.startsWith(path));
};
</script>

<template>
  <nav class="w-full min-h-16 h-16 justify-between flex px-6 xl:px-32 py-10 z-10 bg-fontys text-white"> 
    <ul class="flex h-full items-center gap-4">
      <li class="hidden sm:block mr-8">
        <RouterLink to="/" class="h-12">
          <img src="/logo-white.png" alt="Fontys Logo" class="h-12">
        </RouterLink>
      </li>
      <li>
        <RouterLink :to="{name: 'home'}" :class="[{ 'border-white': isActive('/').value, 'border-transparent hover:text-gray-200': !isActive('/').value }, 'inline-flex items-center px-1 pt-1 border-b-2 text-white text-lg']">Home</RouterLink>
      </li>
      <li v-if="user">
        <RouterLink :to="'/explorer/' + user.root" :class="[{ 'border-white': roughMatch('/explorer').value, 'border-transparent hover:text-gray-200': !roughMatch('/explorer').value }, 'inline-flex items-center px-1 pt-1 border-b-2 text-white text-lg']">Explorer</RouterLink>
      </li>
      <li class="hidden sm:block">
        <span :class="[{ 'hidden': !roughMatch('/edit').value}, 'border-white inline-flex items-center px-1 pt-1 border-b-2 text-white text-lg']" role="button">Edit</span>
      </li>
    </ul>
    <ul class="flex h-full items-center gap-4 text-lg">
      <li v-if="!user">
        <RouterLink :to="{name: 'login'}" :class="[{ 'border-white': isActive('/login').value, 'border-transparent hover:text-gray-200': !isActive('/login').value }, 'inline-flex items-center px-1 pt-1 border-b-2 text-white text-lg']">Login</RouterLink>
      </li>
      <li v-else>
        <UserPopper/>
      </li>
    </ul>
  </nav>
</template>

<style scoped>

</style>