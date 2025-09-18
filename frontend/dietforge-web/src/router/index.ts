import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router';
import Home from '@/pages/HomePage.vue';

const routes: RouteRecordRaw[] = [
  { path: '/', name: 'home', component: Home },
  {
    path: '/calculations',
    name: 'calculations',
    component: () => import('@/pages/CalculationPage.vue'),
  },
  {
    path: '/foods',
    name: 'foods',
    component: () => import('@/pages/FoodsListPage.vue')
  }
];

export default createRouter({
  history: createWebHistory(),
  routes,
});
