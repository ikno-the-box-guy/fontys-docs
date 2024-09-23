<script setup lang="ts">

</script>

<template>
  <nav class="w-full shadow-lg h-16 justify-between flex pl-4 pr-4 z-10"> 
    <ul class="flex h-full items-center gap-4">
      <li>
        <RouterLink :to="{name: 'home'}">Home</RouterLink>
      </li>
    </ul>
    <ul class="flex h-full items-center gap-4">
      <li>
        <RouterLink :to="{name: 'login'}">Login</RouterLink>
      </li>
    </ul>
  </nav>
</template>

<style scoped>

</style>