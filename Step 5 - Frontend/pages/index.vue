<template>
  <div>
    <Tutorial nome="Primeiro tutorial"/>
    <Tutorial nome="Segundo tutorial"/>
  </div>
</template>

<script>
export default {
  layout: 'MainPage',
  middleware: 'authenticated'
}
</script>
