import {useStorage} from "@vueuse/core";

const token = useStorage(
    'token', 
    '', 
    localStorage,
    {
        mergeDefaults: '',
    }
)

export default token;