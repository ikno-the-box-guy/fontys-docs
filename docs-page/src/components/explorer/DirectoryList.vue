<script setup lang="ts">
import {Directory} from "../../entities/Directory.ts";
import InputModal from "../modals/InputModal.vue";
import {ref, watch} from "vue";
import {documentApi} from "../../api/AxiosInstances.ts";
import {useRoute} from "vue-router";

const props = defineProps<{
  parentId: string;
  subdirectories: Directory[];
}>();

const buttons = [
  { label: 'Confirm', action: 'onConfirm' },
];

const show = ref(false);
const errorMessage = ref('');

const handleButtonClick = ({action, inputValue}: event) => {
  errorMessage.value = '';
  if (action === 'onConfirm' && inputValue) {
    documentApi.post("/directories", {
      parentId: props.parentId,
      displayName: inputValue
    }).then((response) => {
      props.subdirectories.push(response.data);
      show.value = false;
    }).catch((error) => {
      if (error.response.status === 409) {
        errorMessage.value = "A directory with this name already exists";
        return;
      }
      
      // Check bad request
      if (error.response.status === 400) {
        errorMessage.value = "Invalid directory name";
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

</script>

<template>
  <ul class="space-y-2">
    <li v-for="dir in subdirectories" :key="dir.id">
      <RouterLink :to="'/explorer/' + dir.id" class="text-blue-600 hover:underline">
        <div class="bg-white p-4 rounded-lg shadow hover:bg-gray-50 transition-colors">
          {{ dir.displayName }}
        </div>
      </RouterLink>
    </li>

    <li>
      <button class="btn btn-green" @click="show = true">
        Create Directory
      </button>
      <span v-if="errorMessage" class="text-red-600 ml-4">{{errorMessage}}</span>
    </li>
  </ul>
  
  <InputModal 
      v-model="show" 
      title="Create Directory" 
      description="Enter the name of the new directory" 
      :buttons="buttons" 
      :show-input="true" 
      input-placeholder="Directory Name"
      @close="show = false"
      @button-click="handleButtonClick"
  />
  
</template>

<style scoped>

</style>