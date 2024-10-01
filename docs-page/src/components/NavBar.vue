<script setup lang="ts">
import user from "../utils/localUser.ts";
import Popper from "vue3-popper";
import UserPopper from "./UserPopper.vue";
</script>

<template>
  <nav class="w-full shadow-lg h-16 justify-between flex pl-4 pr-4 z-10"> 
    <ul class="flex h-full items-center gap-4">
      <li>
        <RouterLink :to="{name: 'home'}">Home</RouterLink>
      </li>
    </ul>
    <ul class="flex h-full items-center gap-4">
      <li v-if="!user">
        <RouterLink :to="{name: 'login'}">Login</RouterLink>
      </li>
      <li v-else>
        <UserPopper/>
      </li>
    </ul>
  </nav>
</template>

<style scoped>

</style>