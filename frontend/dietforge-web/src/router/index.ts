import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router';
import Home from '@/pages/HomePage.vue';

const routes: RouteRecordRaw[] = [
  { path: '/', name: 'home', component: Home },
  {
    path: '/calculos',
    name: 'calculations',
    component: () => import('@/pages/CalculationPage.vue'),
  },
];

export default createRouter({
  history: createWebHistory(),
  routes,
});
