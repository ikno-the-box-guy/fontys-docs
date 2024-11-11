import {useStorage} from "@vueuse/core";

interface LocalUser {
    name: string;
    email: string;
    root: number;
    expiration: number;
}

const user = useStorage<LocalUser>(
    'user',
    null,
    localStorage,
    {
        mergeDefaults: true,
        serializer: {
            read: (v: any): LocalUser | null => v ? JSON.parse(atob(v)) : null,
            write: (v: LocalUser) => btoa(JSON.stringify(v)),
        },
    }
)

export default user;