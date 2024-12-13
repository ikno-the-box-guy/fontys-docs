<script setup lang="ts">
import InputModal from "../modals/InputModal.vue";
import {computed, ref, watch} from "vue";
import {documentApi} from "../../api/AxiosInstances.ts";
import {useRoute} from "vue-router";
import {ArrowDownTrayIcon, DocumentIcon} from "@heroicons/vue/24/outline";

const props = defineProps<{
  parentId: string;
  documents: Document[];
}>();

const buttons = [
  { label: 'Confirm', action: 'onConfirm' },
];

const show = ref(false);
const errorMessage = ref('');

const handleButtonClick = ({action, inputValue}: event) => {
  errorMessage.value = '';
  if (action === 'onConfirm' && inputValue) {
    documentApi.post("/documents", {
      parentId: props.parentId,
      displayName: inputValue
    }).then((response) => {
      props.documents.push(response.data);
      show.value = false;
    }).catch((error) => {
      if (error.response.status === 409) {
        errorMessage.value = "A document with this name already exists";
        return;
      }

      // Check bad request
      if (error.response.status === 400) {
        errorMessage.value = "Invalid document name";
        return;
      }

      errorMessage.value = "An unknown error occurred";
      console.error(error);
    });
  }

  show.value = false;
};

const route = useRoute();
watch(route, () => {
  errorMessage.value = '';
});

const downloadUrl = (id: string) => {
  return computed(() => `${import.meta.env.VITE_DOC_API_URL}/documents/${id}/download`);
};

</script>

<template>
  <ul class="space-y-2 h-full flex flex-col">
    <li v-for="doc in documents" :key="doc.id">
      <RouterLink :to="'/edit/' + doc.id" class="text-blue-600 hover:underline">
        <div class="bg-white p-4 rounded-lg shadow hover:bg-gray-50 transition-colors flex flex-row justify-between group">
          <div class="flex flex-row">
            <DocumentIcon class="h-6 w-6 text-blue-600 mr-2"/>
            <span>{{ doc.displayName }}</span>
          </div>
          <a :href="downloadUrl(doc.id).value" @click.stop class="hidden group-hover:inline">
            <ArrowDownTrayIcon class="h-6 w-6 text-gray-500 hover:text-blue-700"/>
          </a>
        </div>
      </RouterLink>
    </li>

    <li :class="[{'pt-3': documents?.length}, '!mt-auto']">
      <button class="btn btn-fontys" @click="show = true">
        Create Document
      </button>
      <span v-if="errorMessage" class="text-red-600 ml-4">{{errorMessage}}</span>
    </li>
  </ul>

  <InputModal
      v-model="show"
      title="Create Document"
      description="Enter the name of the new document"
      :buttons="buttons"
      :show-input="true"
      input-placeholder="Document Name"
      @close="show = false"
      @button-click="handleButtonClick"
  />
</template>

<style scoped>

</style>
