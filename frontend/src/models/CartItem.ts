import type {ProductResponse} from "../generated-api";

export type CartItem = {
    product: ProductResponse;
    quantity: number;
};