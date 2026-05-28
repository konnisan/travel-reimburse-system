<template>
  <main class="login-page">
    <section class="login-shell">
      <div class="login-copy">
        <p>Travel Reimburse</p>
        <h1>差旅报销权限中心</h1>
        <span>使用账号登录后进入报销工作台。</span>
      </div>

      <el-form ref="formRef" class="login-card" :model="form" :rules="rules" @keyup.enter="handleLogin">
        <div class="card-title">
          <h2>登录</h2>
          <el-tag effect="light">JWT</el-tag>
        </div>
        <el-form-item prop="username">
          <el-input v-model.trim="form.username" size="large" placeholder="用户名" :prefix-icon="User" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="form.password"
            size="large"
            type="password"
            placeholder="密码"
            show-password
            :prefix-icon="Lock"
          />
        </el-form-item>
        <div class="login-options">
          <el-checkbox v-model="remember">记住登录</el-checkbox>
          <el-text type="info">可用账号：admin / admin123</el-text>
        </div>
        <el-button type="primary" size="large" :loading="loading" @click="handleLogin">登录</el-button>
      </el-form>
    </section>
  </main>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import { ElMessage } from 'element-plus'
import { Lock, User } from '@element-plus/icons-vue'
import { useAuthStore } from '@/stores/auth'

const auth = useAuthStore()
const formRef = ref<FormInstance>()
const loading = ref(false)
const remember = ref(true)

const form = reactive({
  username: 'admin',
  password: ''
})

const rules: FormRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const handleLogin = async () => {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return
  loading.value = true
  try {
    await auth.signIn(form)
    ElMessage.success('登录成功')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: grid;
  place-items: center;
  padding: 32px;
  background:
    linear-gradient(135deg, rgba(14, 165, 233, 0.18), rgba(34, 197, 94, 0.12)),
    #f5f7fa;
}

.login-shell {
  width: min(920px, 100%);
  min-height: 420px;
  display: grid;
  grid-template-columns: 1.1fr 420px;
  overflow: hidden;
  border: 1px solid #dbe3ef;
  border-radius: 8px;
  background: #fff;
  box-shadow: 0 22px 70px rgba(15, 23, 42, 0.14);
}

.login-copy {
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 48px;
  background: #0f766e;
  color: #ecfeff;
}

.login-copy p {
  margin: 0 0 18px;
  font-weight: 700;
  letter-spacing: 0;
}

.login-copy h1 {
  margin: 0 0 16px;
  font-size: 34px;
  line-height: 1.2;
}

.login-copy span {
  color: #ccfbf1;
}

.login-card {
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 42px;
}

.card-title,
.login-options {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
}

.card-title {
  margin-bottom: 22px;
}

.card-title h2 {
  margin: 0;
  font-size: 24px;
}

.login-options {
  margin: 4px 0 22px;
}

.login-card > .el-button {
  width: 100%;
}

@media (max-width: 820px) {
  .login-shell {
    grid-template-columns: 1fr;
  }

  .login-copy {
    padding: 30px;
  }
}
</style>
