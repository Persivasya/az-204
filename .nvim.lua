local Utils = require("utils")

vim.g.newfile_basepath = "/unit-1-1/azfunction/src/main/java/com/example/azfunction"

vim.keymap.set("n", "<leader>nf", function()
    Utils.new_from_root(vim.g.newfile_basepath)
  end, { desc = "New function file (from root)" })
-- / small test comment