echo "What do you want to do with node_modules on native library?"
folder_name=native-navigation;
select yn in "Install" "Remove"; do
    case $yn in 
        Install ) 
            cp -r ./node_modules/ ./$folder_name/node_modules
            echo "Node_modules installed on $folder_name"
            exit;;
        Remove )
            rm -rf ./$folder_name/node_modules
            echo "Node_modules removed from $folder_name"
            exit;;
    esac
done