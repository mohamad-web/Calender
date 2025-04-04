<template>
  <div class="login-container">
    <h2>Login</h2>
    <form @submit.prevent="handleLogin">
      <div>
        <label for="username">Benutzername:</label>
        <input type="text" id="username" v-model="username" required />
      </div>
      <div>
        <label for="password">Passwort:</label>
        <input type="password" id="password" v-model="password" required />
      </div>
      <button type="submit">Einloggen</button>
    </form>
    <p v-if="error" class="error">{{ error }}</p>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      username: '',
      password: '',
      error: null
    };
  },
  methods: {
    async handleLogin() {
      try {
        const response = await axios.post('/api/auth/login', {
          username: this.username,
          password: this.password
        });
        alert(response.data); // Erfolgsmeldung
      } catch (err) {
        this.error = 'Login fehlgeschlagen. Überprüfen Sie Ihre Anmeldedaten.';
      }
    }
  }
};
</script>

<style scoped>
.login-container {
  max-width: 300px;
  margin: auto;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 5px;
}
.error {
  color: red;
}
</style>
