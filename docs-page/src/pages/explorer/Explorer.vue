<script setup lang="ts">
import {onBeforeMount, ref, watch} from "vue";
import {useRoute} from "vue-router";
import {Directory} from "../../entities/Directory.ts";
import {Document} from "../../entities/Document.ts";
import { ArrowLeftIcon } from "@heroicons/vue/24/outline";
import axios from "axios";
import DirectoryList from "../../components/explorer/DirectoryList.vue";
import DocumentList from "../../components/explorer/DocumentList.vue";
import {documentApi} from "../../api/AxiosInstances.ts";

const route = useRoute();

const directory = ref<Directory>();
const subdirectories = ref<Directory[]>();
const documents = ref<Document[]>();

const fetchDirectoryInfo = async () => {
  documentApi.get("/directories/" + route.params.directory)
  .then((response) => {
    directory.value = response.data;
  }).catch((error) => {
    console.error(error);
  });
}

const fetchSubdirectories = async () => {
  documentApi.get("/directories/sub/" + route.params.directory)
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

    <div class="flex flex-col md:flex-row w-full space-y-4 md:space-x-4 md:space-y-0">
      <div class="flex-col w-full md:w-1/2 rounded border-2 p-4">
        
        <div v-if="subdirectories && directory">
          <DirectoryList @refresh="fetchSubdirectories" :parent-id="directory.id" :subdirectories="subdirectories"/>
        </div>
        <div v-else class="text-gray-500">
          Loading directories...
        </div>
        
      </div>
      <div class="flex-col w-full md:w-1/2 rounded border-2 p-4">
        
        <div v-if="documents && directory">
          <DocumentList documents="documents"/>
        </div>
        <div v-else class="text-gray-500">
          Loading documents...
        </div>
        
      </div>
    </div>
  </div>
</template>

<style scoped>
</style>