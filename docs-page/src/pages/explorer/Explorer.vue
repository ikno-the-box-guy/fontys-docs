<script setup lang="ts">
import {onBeforeMount, onMounted, ref, watch} from "vue";
import {useRoute} from "vue-router";
import {Directory} from "../../entities/Directory.ts";
import { ArrowLeftIcon } from "@heroicons/vue/24/outline";
import axios from "axios";

const route = useRoute();

const directory = ref<Directory>();
const subdirectories = ref<Directory[]>();

const fetchDirectoryInfo = async () => {
  axios.get(import.meta.env.VITE_DOC_API_URL + "/directories/" + route.params.directory, {
    withCredentials: true,
  })
  .then((response) => {
    directory.value = response.data;
  }).catch((error) => {
    console.error(error);
  });
}

const fetchSubdirectories = async () => {
  axios.get(import.meta.env.VITE_DOC_API_URL + "/directories/sub/" + route.params.directory, {
    withCredentials: true,
  })
      .then((response) => {
        subdirectories.value = response.data;
      }).catch((error) => {
    console.error(error);
  });
}

const fetchDocuments = async () => {
  
}

onBeforeMount(() => {
  fetchDirectoryInfo();
  fetchSubdirectories();
  fetchDocuments();
});

watch(route, () => {
  fetchDirectoryInfo();
  fetchSubdirectories();
  fetchDocuments();
});
</script>

<template>
  <div class="p-6 bg-gray-100 min-h-screen">
    <h1 class="text-3xl font-bold mb-4">Explorer</h1>

    <div v-if="directory && directory.id !== directory.parentId" class="mb-6">
      <!-- Link to Parent Directory -->
      <RouterLink :to="'/explorer/' + directory.parentId" class="text-blue-600 hover:underline flex items-center mb-4">
        <ArrowLeftIcon class="h-6 w-6 mr-2"/>
        Go Back
      </RouterLink>

      <!-- Current Directory Name -->
      <h2 class="text-xl font-semibold text-gray-800">{{ directory.displayName }}</h2>
    </div>

    <div v-if="subdirectories && directory">
      <ul class="space-y-2">
        <li v-for="dir in subdirectories" :key="dir.id" class="bg-white p-4 rounded-lg shadow hover:bg-gray-50 transition-colors">
          <RouterLink :to="'/explorer/' + dir.id" class="text-blue-600 hover:underline">
            {{ dir.displayName }}
          </RouterLink>
        </li>
      </ul>
    </div>

    <div v-else class="text-gray-500">
      Loading directories...
    </div>
  </div>
</template>

<style scoped>

</style>